package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.TopicVO;
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
public class TopicDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int barId, final String content) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into topic " +
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


    public List<TopicVO> list(int barId, int pageName, int size) {
        String sql = "select user_id,bar_id,content,createtime from topic where deleted=? and bar_id=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageName * size, size}, new RowMapper<TopicVO>() {
            @Override
            public TopicVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                TopicVO vo = new TopicVO();
                vo.setUserId(rs.getInt(1));
                vo.setBarId(rs.getInt(2));
                vo.setContent(rs.getString(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                return vo;
            }
        });
    }


}
