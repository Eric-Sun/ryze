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

    public int add(final int userId, final int barId, final int postId, final String content) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into reply " +
                "(user_id,bar_id,content,createtime,updatetime,post_id) " +
                "values" +
                "(?,?,?,now(),now(),?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, barId);
                pstmt.setString(3, content);
                pstmt.setInt(4, postId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public List<ReplyVO> list(int barId, int postId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id from reply where deleted=? and bar_id=? and post_id=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, barId, postId, pageName * size, size}, new RowMapper<ReplyVO>() {
            @Override
            public ReplyVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ReplyVO vo = new ReplyVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                vo.setReplyId(rs.getInt(5));
                vo.setPostId(rs.getInt(6));
                return vo;
            }
        });
    }


    public void updateContent(int replyId, String content) {
        String sql = "update reply set content=? where id=? and deleted=?";
        j.update(sql, new Object[]{content, replyId, Constants.DB.NOT_DELETED});
    }

    public void delete(int replyId) {
        String sql = "update reply set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, replyId, Constants.DB.NOT_DELETED});
    }


}
