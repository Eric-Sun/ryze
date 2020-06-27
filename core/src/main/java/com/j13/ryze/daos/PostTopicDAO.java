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
import java.util.List;

@Repository
public class PostTopicDAO {
    @Autowired
    JdbcTemplate j;

    public int insert(final int postId, final int topicId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into post_topic (post_id,topic_id,createtime,updatetime)" +
                " values (?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, postId);
                pstmt.setInt(2, topicId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public List<Integer> getAllTopicIds(int postId) {
        String sql = "select topic_id from post_topic where post_id=? and deleted=?";
        return j.queryForList(sql, new Object[]{postId, Constants.DB.NOT_DELETED}, Integer.class);
    }

    public void deletePostTopic(int postTopicId) {
        String sql = "update post_topic set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.NOT_DELETED, postTopicId});
    }

    public void deleteByTopicId(int topicId) {
        String sql = "update post_topic set deleted=? where topic_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, topicId});
    }

    public void deleteByTopicIdAndPostId(int topicId, int postId) {
        String sql = "update post_topic set deleted=? where topic_id=? and post_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, topicId, postId});
    }

}
