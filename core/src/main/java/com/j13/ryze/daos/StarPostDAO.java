package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.StarPostVO;
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
public class StarPostDAO {

    @Autowired
    JdbcTemplate j;


    public int add(final int postId, final int value) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into star_post (post_id,`value`) values (?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, postId);
                pstmt.setInt(2, value);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public void delete(int id) {
        String sql = "update star_post set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }

    public List<StarPostVO> list() {
        String sql = "select id,post_id,`value` from star_post where deleted=?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED}, new RowMapper<StarPostVO>() {
            @Override
            public StarPostVO mapRow(ResultSet resultSet, int i) throws SQLException {
                StarPostVO vo = new StarPostVO();
                vo.setId(resultSet.getInt(1));
                vo.setPostId(resultSet.getInt(2));
                vo.setValue(resultSet.getInt(3));
                return vo;
            }
        });
    }

    public boolean checkStar(int postId) {
        String sql = "select count(1) from star_post where post_id=? and deleted=?";
        int cnt = j.queryForObject(sql, new Object[]{postId, Constants.DB.NOT_DELETED}, Integer.class);
        return cnt == 0 ? false : true;
    }

    public void deleteByPostId(int postId) {
        String sql = "update star_post set deleted=? where post_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, postId});
    }

    public List<Integer> listPostId() {
        String sql = "select post_id from star_post where deleted=?";
        return j.queryForList(sql, new Object[]{Constants.DB.NOT_DELETED}, Integer.class);
    }
}
