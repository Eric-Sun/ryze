package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BarVO;
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
public class BarDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into bar " +
                "(user_id,name,createtime,updatetime) " +
                "values" +
                "(?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, name);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public void delete(int barId) {
        String sql = "update bar set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, barId});
    }


    public void updateContent(int userId, int barId, String content) {
        String sql = "update bar set content=? where user_id=? and id=? and deleted=?";
        j.update(sql, new Object[]{content, userId, barId, Constants.DB.NOT_DELETED});
    }


    public List<BarVO> list(int size, int pageNum) {
        String sql = "select id,name,createtime,user_id from bar where deleted=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<BarVO>() {
            @Override
            public BarVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BarVO vo = new BarVO();
                vo.setBarId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                vo.setUserId(rs.getInt(4));
                return vo;
            }
        });
    }


    public boolean checkBarOwner(int barId, int userId) {
        String sql = "select count(1) from bar where id=? and user_id=? and deleted=?";
        int count = j.queryForObject(sql, new Object[]{barId, userId, Constants.DB.NOT_DELETED}, Integer.class);
        return count == 0 ? false : true;
    }


    public boolean exist(int barId) {
        String sql = "select count(1) from bar where deleted=? and id=?";
        int count = j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED, barId}, Integer.class);
        return count == 0 ? false : true;
    }


    public List<BarVO> queryForBarName(String queryBarName, int size, int pageNum) {
        String sql = "select id,name,createtime,user_id from bar where name like ? and deleted=? limit ?,?";
        return j.query(sql, new Object[]{"%" + queryBarName + "%", Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<BarVO>() {
            @Override
            public BarVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BarVO vo = new BarVO();
                vo.setBarId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                vo.setUserId(rs.getInt(4));
                return vo;
            }
        });
    }


    public String getBarName(int barId) {
        String sql = "select name from bar where id=?";
        return j.queryForObject(sql, new Object[]{barId}, String.class);

    }
}
