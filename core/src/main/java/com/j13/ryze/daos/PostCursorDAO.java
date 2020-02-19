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

    public long add(final String userToken, final int postId, final int cursor, final int pageNum) {
        KeyHolder holder = new GeneratedKeyHolder();

        final String sql = "insert into post_cursor (user_token,post_id,`cursor`,page_num)" +
                " values (?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, userToken);
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
     * @param userToken
     * @param postId
     * @return
     */
    public boolean checkExist(String userToken, int postId) {
        String sql = "select count(1) from post_cursor where user_token=? and post_id=?";
        return j.queryForObject(sql, new Object[]{userToken, postId}, Integer.class) == 0 ? false : true;
    }


    public PostCursorVO getCursor(String userToken, int postId) {
        String sql = "select `cursor`,page_num from post_cursor where user_token=? and post_id=?";
        return j.queryForObject(sql, new Object[]{userToken, postId}, new RowMapper<PostCursorVO>() {
            @Override
            public PostCursorVO mapRow(ResultSet resultSet, int i) throws SQLException {
                PostCursorVO vo = new PostCursorVO();
                vo.setCursor(resultSet.getInt(1));
                vo.setPageNum(resultSet.getInt(2));
                return vo;
            }
        });
    }


    public void updateCursor(String userToken, int postId, int cursor, int pageNum) {
        String sql = "update post_cursor set `cursor`=?,page_num=? where user_token=? and post_id=?";
        j.update(sql, new Object[]{cursor, pageNum, userToken, postId});
    }
}
