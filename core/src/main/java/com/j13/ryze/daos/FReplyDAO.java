package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.fetcher.FReplyVO;
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
import java.util.List;

@Repository
public class FReplyDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int fPostId, final int lastFReplyId, final String content, final int sourceReplyId, final int isAuthor) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into f_reply " +
                "(f_post_id,last_f_reply_id,fetch_time,content,source_reply_id,is_author) " +
                "values" +
                "(?,?,now(),?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, fPostId);
                pstmt.setInt(2, lastFReplyId);
                pstmt.setString(3, content);
                pstmt.setInt(4, sourceReplyId);
                pstmt.setInt(5, isAuthor);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void updateStatusAndReplyId(int fReplyId, int status, int replyId) {
        String sql = "update f_reply set status=?,reply_id=? where id=?";
        j.update(sql, new Object[]{status, replyId, fReplyId});
    }

    public boolean checkExist(int sourceReplyId) {
        String sql = "select count(1) from f_reply where source_reply_id=?";
        int count = j.queryForObject(sql, new Object[]{sourceReplyId}, Integer.class);
        return count == 0 ? false : true;

    }

    public int findFReplyId(Integer sourceReplyId) {
        String sql = "select id from f_reply where source_reply_id=?";
        return j.queryForObject(sql, new Object[]{sourceReplyId}, Integer.class);
    }

    public List<FReplyVO> findReplysByFPostId(int fPostId, int replyCount) {
        String sql = "select id,f_post_id,last_f_reply_id,source_reply_id,content,is_author " +
                "from f_reply where f_post_id=? and status=? order by id asc limit ?";
        return j.query(sql, new Object[]{fPostId, Constants.Fetcher.Status.NOT_PUSH, replyCount}, new RowMapper<FReplyVO>() {
            @Override
            public FReplyVO mapRow(ResultSet resultSet, int i) throws SQLException {
                FReplyVO vo = new FReplyVO();
                vo.setId(resultSet.getInt(1));
                vo.setfPostId(resultSet.getInt(2));
                vo.setLastFReplyId(resultSet.getInt(3));
                vo.setSourceReplyId(resultSet.getInt(4));
                vo.setContent(resultSet.getString(5));
                vo.setIsAuhtor(resultSet.getInt(6));
                return vo;
            }
        });
    }

    public int findReplyIdFromFReplyId(int lastFReplyId) {
        String sql = "select reply_id from f_reply where id=?";
        return j.queryForObject(sql, new Object[]{lastFReplyId}, Integer.class);
    }

    public int countReplyCount(Integer fPostId) {
        String sql = "select count(1) from f_reply where f_post_id=?";
        return j.queryForObject(sql, new Object[]{fPostId}, Integer.class);
    }
}
