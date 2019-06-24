package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BarVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
                   final String content, final int anonymous, final int lastReplyId, final String imgListStr) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into reply " +
                "(user_id,bar_id,content,createtime,updatetime,post_id,anonymous,last_reply_id,img_list) " +
                "values" +
                "(?,?,?,now(),now(),?,?,?,?)";
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
                pstmt.setString(7, imgListStr);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public List<ReplyVO> list(int postId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id,img_list " +
                "from reply where deleted=? and post_id=? and last_reply_id=0 order by createtime ASC limit ?,? ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, postId, pageName * size, size}, new ReplyRowMapper());
    }

    public List<ReplyVO> lastReplylist(int lastReplyId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "post_id,anonymous,last_reply_id,img_list from reply where deleted=? and last_reply_id=?  order by updatetime asc limit ?,? ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, lastReplyId, pageName * size, size}, new ReplyRowMapper());
    }

    public int lastReplylistSize(int lastReplyId) {
        String sql = "select count(1) from reply where deleted=? and " +
                "last_reply_id=?  ";
        return j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED, lastReplyId}, Integer.class);
    }

    public void update(int replyId, String content, int anonymous, String imgListStr) {
        String sql = "update reply set content=?,anonymous=?,img_list=? where id=? and deleted=?";
        j.update(sql, new Object[]{content, anonymous, imgListStr, replyId, Constants.DB.NOT_DELETED});
    }

    public void delete(int replyId) {
        String sql = "update reply set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, replyId, Constants.DB.NOT_DELETED});
    }


    public ReplyVO get(int replyId) {
        try {
            String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id,img_list" +
                    " from reply where deleted=? and id=?";
            return j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED, replyId}, new ReplyRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Integer> recentlyList(int barId, int userId, int pageNum, int size) {
        String sql = "select post_id from reply where bar_id=? and user_id=? and deleted=? " +
                "order by updatetime desc limit ?,?";
        return j.queryForList(sql, new Object[]{barId, userId, Constants.DB.NOT_DELETED, pageNum * size, size}, Integer.class);
    }

    public int replyCount(int postId) {
        String sql = "select count(1) from reply where post_id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{postId, Constants.DB.NOT_DELETED}, Integer.class);
    }

    /**
     * 获取一级评论的总数量
     * @param postId
     * @return
     */
    public int level1ReplyCount(int postId) {
        String sql = "select count(1) from reply where post_id=? and last_reply_id=0 and deleted=?";
        return j.queryForObject(sql, new Object[]{postId, Constants.DB.NOT_DELETED}, Integer.class);
    }


    public List<ReplyVO> listByBarId(int barId, int pageNum, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id,img_list " +
                "from reply where deleted=? and bar_id=? order by createtime DESC limit ?,? ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, barId, pageNum * size, size}, new ReplyRowMapper());
    }

    public void delete(int userId, int replyId) {
        String sql = "update reply set deleted=? where id=? and user_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, replyId, userId});
    }

    public List<ReplyVO> reverselist(int postId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id,post_id,anonymous,last_reply_id,img_list " +
                "from reply where deleted=? and post_id=? and last_reply_id=0 order by createtime desc limit ?,? ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, postId, pageName * size, size}, new ReplyRowMapper());
    }

    class ReplyRowMapper implements RowMapper<ReplyVO> {

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
            vo.setImgListStr(rs.getString(9));
            return vo;
        }
    }
}

