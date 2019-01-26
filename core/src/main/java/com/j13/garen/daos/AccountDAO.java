package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.AccountVO;
import com.j13.garen.vos.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountDAO {


    @Autowired
    JdbcTemplate j;

    public int add(final String accountName, final String passwordAfterMD5, final int authorityId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into account " +
                "(name,password,createtime,updatetime) " +
                "values" +
                "(?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, accountName);
                pstmt.setString(2, passwordAfterMD5);
                return pstmt;
            }
        }, holder);
        final int accountId = holder.getKey().intValue();

        // 给account赋权
        final String authSql = "insert into account_authority " +
                "(account_id,authority_id,createtime,updatetime) " +
                "values" +
                "(?,?,now(),now())";

        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(authSql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, authorityId);
                return pstmt;
            }
        }, holder);
        return accountId;
    }


    public void delete(int accountId) {
        String updateAccountAuthoritySql = "update account_authority set deleted= ?,updatetime=now() where account_id=?";
        j.update(updateAccountAuthoritySql, new Object[]{Constants.DB.DELETED, accountId});

        String updateAccountSql = "update account set deleted=?,updatetime=now() where id=?";
        j.update(updateAccountSql, new Object[]{Constants.DB.DELETED, accountId});
    }

//    public PainterVO getPainter(int accountId) {
//        String sql = "select a.name,p.brief,mobile,p.real_name,a.id from account a " +
//                "left outer join painter_info p on p.account_id=a.id where a.id=? and a.deleted=? and p.deleted=?";
//        PainterVO vo = j.queryForObject(sql, new Object[]{accountId, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<PainterVO>() {
//            @Override
//            public PainterVO mapRow(ResultSet rs, int rowNum) throws SQLException {
//                PainterVO vo = new PainterVO();
//                vo.setAccountName(rs.getString(1));
//                vo.setBrief(rs.getString(2));
//                vo.setMobile(rs.getString(3));
//                vo.setRealName(rs.getString(4));
//                vo.setAccountId(rs.getInt(5));
//                return vo;
//            }
//        });
//        return vo;
//    }


    public List<AuthorityVO> getAuthorityListByAccountName(String name) {
        String sql = "select a.id,a.name from account u , authority a, account_authority ua " +
                " where u.name=? and u.id=ua.account_id and ua.authority_id = a.id and a.deleted=? and ua.deleted=?";
        return j.query(sql, new Object[]{name, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<AuthorityVO>() {
            @Override
            public AuthorityVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AuthorityVO vo = new AuthorityVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                return vo;
            }
        });
    }

    public int getAccountIdByAccountName(String name) {
        String sql = "select id from account where name=? and deleted=?";
        return j.queryForObject(sql, new Object[]{name, Constants.DB.NOT_DELETED}, Integer.class);
    }

    public boolean checkExisted(String name, String password) {
        String sql = "select count(1) from account where name=? and password=? and deleted=?";
        return j.queryForObject(sql, new Object[]{name, password, Constants.DB.NOT_DELETED}, Integer.class) == 0 ? false : true;
    }

    public boolean checkExisted(String name) {
        String sql = "select count(1) from account where name=?  and deleted=?";
        return j.queryForObject(sql, new Object[]{name, Constants.DB.NOT_DELETED}, Integer.class) == 0 ? false : true;
    }

    public List<AccountVO> list() {
        String sql = "select a.id ,a.name,a.createtime,aa.authority_id,auth.name from account a " +
                "left outer join account_authority aa  on aa.account_id=a.id " +
                "left outer join authority auth on auth.id=aa.authority_id" +
                " where a.deleted=? and aa.deleted=?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<AccountVO>() {
            @Override
            public AccountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AccountVO vo = new AccountVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setCreatetime(rs.getDate(3).getTime());
                vo.setAuthorityId(rs.getInt(4));
                vo.setAuthorityName(rs.getString(5));
                return vo;
            }
        });
    }


    public AccountVO get(int id) {
        String sql = "select a.id ,a.name,a.createtime,aa.authority_id from account a " +
                "left outer join account_authority aa on aa.account_id=a.id" +
                " where a.id=? and a.deleted=? and aa.deleted=?";
        return j.queryForObject(sql, new Object[]{id, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<AccountVO>() {
            @Override
            public AccountVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                AccountVO vo = new AccountVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setCreatetime(rs.getDate(3).getTime());
                vo.setAuthorityId(rs.getInt(4));
                return vo;
            }
        });
    }


    /**
     * update account basic info. if the painter need to update the painter info from PainterDAO
     *
     * @param id
     * @param name
     * @param passwordAfterMD5 after md5
     * @param authorityId
     */
    public void update(int id, String name, String passwordAfterMD5, int authorityId) {
        String updateAccount = "update account set name=?,password=?,updatetime=now() where id=? and deleted=? ";
        j.update(updateAccount, new Object[]{name, passwordAfterMD5, id, Constants.DB.NOT_DELETED});

        String updateAccountAuthority = "update account_authority set authority_id=?,updatetime=now() where " +
                "account_id=? and deleted=?";
        j.update(updateAccountAuthority, new Object[]{authorityId, id, Constants.DB.NOT_DELETED});
    }


}
