package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BarVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
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
public class ReplyDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int barId, final int postId,
                   final String content, final int anonymous, final int lastReplyId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into reply " +
                "(user_id,bar_id,content,createtime,updatetime,post_id,anonymous,last_reply_id) " +
                "values" +
                "(?,?,?,now(),now(),?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, barId);
                pstmt.setString(3, content);
                pstmt.setInt(4, postId);
                pstmt.setInt(5, anonymous);
                pstmt.setInt(6, lastReplyId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public List<ReplyVO> list(int postId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id " +
                "from reply where deleted=? and post_id=? and last_reply_id=0 limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, postId, pageName * size, size}, new RowMapper<ReplyVO>() {
            @Override
            public ReplyVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReplyVO vo = new ReplyVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                vo.setReplyId(rs.getInt(5));
                vo.setPostId(rs.getInt(6));
                vo.setAnonymous(rs.getInt(7));
                vo.setLastReplyId(rs.getInt(8));
                return vo;
            }
        });
    }

    public List<ReplyVO> lastReplylist(int lastReplyId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id from reply where deleted=? and last_reply_id=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, lastReplyId, pageName * size, size}, new RowMapper<ReplyVO>() {
            @Override
            public ReplyVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReplyVO vo = new ReplyVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                vo.setReplyId(rs.getInt(5));
                vo.setPostId(rs.getInt(6));
                vo.setAnonymous(rs.getInt(7));
                vo.setLastReplyId(rs.getInt(8));
                return vo;
            }
        });
    }


    public void update(int replyId, String content, int anonymous) {
        String sql = "update reply set content=?,anonymous=? where id=? and deleted=?";
        j.update(sql, new Object[]{content, anonymous, replyId, Constants.DB.NOT_DELETED});
    }

    public void delete(int replyId) {
        String sql = "update reply set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, replyId, Constants.DB.NOT_DELETED});
    }


    public ReplyVO get(int replyId) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous from reply where deleted=? and id=?";
        return j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED, replyId}, new RowMapper<ReplyVO>() {
            @Override
            public ReplyVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReplyVO vo = new ReplyVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                vo.setReplyId(rs.getInt(5));
                vo.setPostId(rs.getInt(6));
                vo.setAnonymous(rs.getInt(7));
                return vo;
            }
        });
    }

    public List<Integer> recentlyList(int barId, int userId, int pageNum, int size) {
        String sql = "select post_id from reply where bar_id=? and user_id=? and deleted=? " +
                "order by updatetime desc limit ?,?";
        return j.queryForList(sql, new Object[]{barId, userId, Constants.DB.NOT_DELETED, pageNum * size, size}, Integer.class);
    }
}
