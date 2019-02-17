package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BarVO;
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


    public void updateContent(int postId, String content) {
        String sql = "update post set content=? where id=? and deleted=?";
        j.update(sql, new Object[]{content, postId, Constants.DB.NOT_DELETED});
    }

    public void delete(int postId) {
        String sql = "update post set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, postId, Constants.DB.NOT_DELETED});
    }


    public List<PostVO> queryForPostName(String queryPostName, int size, int pageNum) {
        String sql = "select id,createtime,user_id,bar_id from post where name like ? and deleted=? limit ?,?";
        return j.query(sql, new Object[]{"%" + queryPostName + "%", Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<PostVO>() {
            @Override
            public PostVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                PostVO vo = new PostVO();
                vo.setPostId(rs.getInt(1));
                vo.setCreatetime(rs.getTimestamp(2).getTime());
                vo.setUserId(rs.getInt(3));
                vo.setBarId(rs.getInt(4));
                return vo;
            }
        });
    }
}
