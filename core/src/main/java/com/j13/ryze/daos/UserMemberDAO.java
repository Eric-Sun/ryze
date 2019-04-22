package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.UserMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
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

@Repository
public class UserMemberDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int level, final long expiretime) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user_member " +
                "(user_id,level,expiretime,createtime,updatetime) " +
                "values" +
                "(?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, level);
                pstmt.setLong(3, expiretime);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public UserMemberVO getUserMember(int userId) {
        String sql = "select user_id,level,expiretime,createtime from user_member where user_id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, new RowMapper<UserMemberVO>() {
            @Override
            public UserMemberVO mapRow(ResultSet resultSet, int i) throws SQLException {
                UserMemberVO vo = new UserMemberVO();
                vo.setUserId(resultSet.getInt(1));
                vo.setLevel(resultSet.getInt(2));
                vo.setExpiretime(resultSet.getTimestamp(3).getTime());
                vo.setCreatetime(resultSet.getTimestamp(4).getTime());
                return vo;
            }
        });
    }

    /**
     * 检查是否存在这条记录，如果返回0则是不存在
     *
     * @param userId
     * @return
     */
    public int checkExist(int userId) {
        String sql = "select count(1) from user_member where user_id=? and deleted=?";
        return j.update(sql, new Object[]{userId, Constants.DB.DELETED});
    }


    public void updateUserMember(int userId, int level, long expiretime) {
        String sql = "update user_member set level=? and expiretime=? and createtime=now() where user_id=?";
        j.update(sql, new Object[]{level, expiretime, userId});
    }



}
