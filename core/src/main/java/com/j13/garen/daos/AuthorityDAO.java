package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.AuthorityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by sunbo on 14-11-16.
 */
@Repository("authorityDAO")
public class AuthorityDAO {


    @Autowired
    JdbcTemplate j;


    public List<AuthorityVO> list() {
        String sql = "SELECT * FROM authority ";
        return j.query(sql, new Object[]{}, new BeanPropertyRowMapper<AuthorityVO>(AuthorityVO.class));
    }


    public int insert(final String authorityName, String[] resourceIdList) {
        final String sql = "insert into authority (name,createtime,updatetime) values (?,now(),now())";
        KeyHolder holder = new GeneratedKeyHolder();
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, authorityName);
                return pstmt;
            }
        }, holder);
        final int id = holder.getKey().intValue();

        for (final String resourceId : resourceIdList) {
            final String sql2 = "insert into authority_resource (authority_id,resource_id,createtime,updatetime) values (?,?,now(),now())";
            j.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement pstmt = con.prepareStatement(sql2);
                    pstmt.setInt(1, id);
                    pstmt.setInt(2, new Integer(resourceId + ""));
                    return pstmt;
                }
            });
        }
        return id;

    }

    public void update(final int id, final String name, String[] resourceIdList) {
        String sql = "update authority set name=?,updatetime=now() where id=?";
        j.update(sql, new Object[]{name, id});


        sql = "delete from authority_resource where authority_id=?";
        j.update(sql, new Object[]{id});


        for (final String resourceId : resourceIdList) {
            final String sql2 = "insert into authority_resource (authority_id,resource_id,createtime,updatetime) values (?,?,now(),now())";
            j.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement pstmt = con.prepareStatement(sql2);
                    pstmt.setInt(1, id);
                    pstmt.setInt(2, new Integer(resourceId + ""));
                    return pstmt;
                }
            });
        }
    }


    public void delete(int id) {
        String sql = "update authority set deleted=?,updatetime=now() where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }

    public AuthorityVO get(int id) {
        String sql = "SELECT * FROM authority where id=?";
        return j.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<AuthorityVO>(AuthorityVO.class));
    }

}
