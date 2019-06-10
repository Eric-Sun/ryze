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


    public void updateStatus(int fPostId, int status) {
        String sql = "update f_post set status=? where id=?";
        j.update(sql, new Object[]{status, fPostId});
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
        })
    }
}
