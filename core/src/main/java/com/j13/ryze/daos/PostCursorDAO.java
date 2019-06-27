package com.j13.ryze.daos;

import com.j13.ryze.vos.PostCursorVO;
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

@Repository
public class PostCursorDAO {


    @Autowired
    JdbcTemplate j;

    public long add(final int userId, final int postId, final int cursor, final int pageNum) {
        KeyHolder holder = new GeneratedKeyHolder();

        final String sql = "insert into post_cursor (user_id,post_id,`cursor`,page_num)" +
                " values (?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, postId);
                pstmt.setInt(3, cursor);
                pstmt.setInt(4, pageNum);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    /**
     * 检查是否有对应的cursor
     *
     * @param userId
     * @param postId
     * @return
     */
    public boolean checkExist(int userId, int postId) {
        String sql = "select count(1) from post_cursor where user_id=? and post_id=?";
        return j.queryForObject(sql, new Object[]{userId, postId}, Integer.class) == 0 ? false : true;
    }


    public PostCursorVO getCursor(int userId, int postId) {
        String sql = "select `cursor`,page_num from post_cursor where user_id=? and post_id=?";
        return j.queryForObject(sql, new Object[]{userId, postId}, new RowMapper<PostCursorVO>() {
            @Override
            public PostCursorVO mapRow(ResultSet resultSet, int i) throws SQLException {
                PostCursorVO vo = new PostCursorVO();
                vo.setCursor(resultSet.getInt(1));
                vo.setPageNum(resultSet.getInt(2));
                return vo;
            }
        });
    }


    public void updateCursor(int userId, int postId, int cursor, int pageNum) {
        String sql = "update post_cursor set `cursor`=?,page_num=? where user_id=? and post_id=?";
        j.update(sql, new Object[]{cursor, pageNum, userId, postId});
    }
}
