package com.j13.ryze.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class StarPostShowlogDAO {


    @Autowired
    JdbcTemplate j;

    public int add(final String userToken, final int postId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into star_post_showlog (user_token,post_id,createtime) values (?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, userToken);
                pstmt.setInt(2, postId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public List<Integer> list(String userToken) {
        String sql = "select post_id from star_post_showlog where user_token=?";
        return j.queryForList(sql, new Object[]{userToken}, Integer.class);
    }
}
