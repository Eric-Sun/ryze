package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.PostVO;
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
public class PostDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int barId, final String content) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into post " +
                "(user_id,bar_id,content,createtime,updatetime) " +
                "values" +
                "(?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, barId);
                pstmt.setString(3, content);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public List<PostVO> list(int barId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime,id from post where deleted=? and bar_id=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, barId, pageName * size, size}, new RowMapper<PostVO>() {
            @Override
            public PostVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                PostVO vo = new PostVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                vo.setPostId(rs.getInt(5));
                return vo;
            }
        });
    }


    public void updateContent(int barId, int postId, String content) {
        String sql = "update post set content=? where id=? and bar_id=? and deleted=?";
        j.update(sql, new Object[]{content, postId, barId, Constants.DB.NOT_DELETED});
    }

    public void delete(int postId) {
        String sql = "update post set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, postId, Constants.DB.NOT_DELETED});
    }


}
