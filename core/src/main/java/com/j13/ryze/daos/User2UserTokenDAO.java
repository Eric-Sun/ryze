package com.j13.ryze.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class User2UserTokenDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final String userToken) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user2user_token (user_id,user_token,createtime) values (?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, userToken);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    /**
     * 如果没有关联的userToken，返回null
     * @param userId
     * @return
     */
    public String getUserToken(int userId) {
        String sql = "select user_token from user2user_token where user_id=?";
        try {
            return j.queryForObject(sql, new Object[]{userId}, String.class);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void updateUserToken(int userId, String userToken) {
        String sql = "update user2user_token set user_token=?,updatetime=now() where user_id=?";
        j.update(sql, new Object[]{userToken, userId});
    }

}
