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

@Repository
public class FReplyDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int fPostId, final int fReplyId, final String content, final int sourceReplyId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into f_reply " +
                "(f_post_id,f_reply_id,fetch_time,content,source_reply_id) " +
                "values" +
                "(?,?,now(),?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, fPostId);
                pstmt.setInt(2, fReplyId);
                pstmt.setString(3, content);
                pstmt.setInt(4, sourceReplyId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void updateStatus(int fReplyId, int status) {
        String sql = "update f_reply set status=? where id=?";
        j.update(sql, new Object[]{status, fReplyId});
    }

    public boolean checkExist(int sourceReplyId) {
        String sql = "select count(1) from f_reply where source_reply_id=?";
        int count = j.queryForObject(sql, new Object[]{sourceReplyId}, Integer.class);
        return count == 0 ? false : true;

    }

    public int findFReplyId(Integer sourceReplyId) {
        String sql = "select source_reply_id from f_reply where id=?";
        return j.queryForObject(sql, new Object[]{sourceReplyId}, Integer.class);
    }
}
