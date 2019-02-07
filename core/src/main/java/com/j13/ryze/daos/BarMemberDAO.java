package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class BarMemberDAO {
    @Autowired
    JdbcTemplate j;

    public int addMember(final int barId, final int userId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into bar_member " +
                "(bar_id,user_id,createtime,updatetime) " +
                "values" +
                "(?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, barId);
                pstmt.setInt(2, userId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public boolean hasMember(int barId, int userId) {
        String sql = "select count(1) from bar_member where bar_id=? and user_id=? and deleted=?";
        int count = j.queryForObject(sql, new Object[]{barId, userId, Constants.DB.NOT_DELETED}, Integer.class);
        return count == 0 ? false : true;
    }


    public void deleteMember(int userId, int barId) {
        String sql = "update bar_member set deleted=? where user_id=? and bar_id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, userId, barId, Constants.DB.NOT_DELETED});
    }
}
