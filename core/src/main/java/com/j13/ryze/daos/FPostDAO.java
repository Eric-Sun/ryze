package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.fetcher.FPostVO;
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
public class FPostDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int sourceType, final int sourcePostId, final String title, final String content) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into f_post " +
                "(source_type,source_post_id,fetch_time,content,title) " +
                "values" +
                "(?,?,now(),?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, sourceType);
                pstmt.setInt(2, sourcePostId);
                pstmt.setString(3, content);
                pstmt.setString(4, title);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public void updateStatusAndPostId(int fPostId, int status, int postId, int userId) {
        String sql = "update f_post set status=?,post_id=?,post_user_id=? where id=?";
        j.update(sql, new Object[]{status, postId, userId, fPostId});
    }

    /**
     * 检测抓取的这个源对应的postId已经存在
     *
     * @param sourcePostId
     * @return
     */
    public boolean checkExist(int sourcePostId) {
        String sql = "select count(1) from f_post where source_post_id=?";
        int count = j.queryForObject(sql, new Object[]{sourcePostId}, Integer.class);
        return count == 0 ? false : true;
    }


    /**
     * 查找一个未插入到正式环境的Post
     *
     * @return
     */
    public FPostVO selectOneUninsertedPost() {
        String sql = "select id,source_post_id,title,content from f_post where status=? limit 1";
        return j.queryForObject(sql, new Object[]{Constants.Fetcher.Status.NOT_PUSH}, new RowMapper<FPostVO>() {
            @Override
            public FPostVO mapRow(ResultSet resultSet, int i) throws SQLException {
                FPostVO vo = new FPostVO();
                vo.setId(resultSet.getInt(1));
                vo.setSourcePostId(resultSet.getInt(2));
                vo.setTitle(resultSet.getString(3));
                vo.setContent(resultSet.getString(4));
                return vo;
            }
        });
    }

    /**
     * 获取已经插入到Post表中的所有的抓取id
     *
     * @return
     */
    public List<FPostVO> selectInsertedPostList() {

        String sql = "select post_id,post_user_id,source_post_id from f_post where status=?";
        return j.query(sql, new Object[]{Constants.Fetcher.Status.PUSHED}, new RowMapper<FPostVO>() {
            @Override
            public FPostVO mapRow(ResultSet resultSet, int i) throws SQLException {
                FPostVO vo = new FPostVO();
                vo.setPostId(resultSet.getInt(1));
                vo.setPostUserId(resultSet.getInt(2));
                vo.setSourcePostId(resultSet.getInt(3));
                return vo;
            }
        });
    }

    public void updateReplyCount(int postId, int addReplyCountPerPost) {
        String sql = "update f_post set reply_count=? where id=?";
        j.update(sql, new Object[]{addReplyCountPerPost, postId});
    }

    public int getFReplyCount(int postId) {
        String sql = "select reply_count from f_post where post_id=?";
        try {
            return j.queryForObject(sql, new Object[]{postId}, Integer.class);
        }catch(Exception e){
            return 0;
        }
    }
}
