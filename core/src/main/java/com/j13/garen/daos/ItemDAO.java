package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.ItemVO;
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
public class ItemDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final String name, final float price) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into item " +
                "(name,price,createtime,updatetime) " +
                "values" +
                "(?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setFloat(2, price);
                return pstmt;
            }
        }, holder);
        final int accountId = holder.getKey().intValue();
        return accountId;
    }

    public void delete(int itemId) {
        String sql = "update item set deleted=?,updatetime=now() where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, itemId});
    }

    public void update(int itemId, String name, float price) {
        String sql = "update item set name=?, price=?,updatetime=now() where id=? and deleted=?";
        j.update(sql, new Object[]{name, price, itemId, Constants.DB.NOT_DELETED});
    }

    public ItemVO get(int itemId) {
        String sql = "select name,price from item where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{itemId, Constants.DB.NOT_DELETED}, new RowMapper<ItemVO>() {
            @Override
            public ItemVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemVO vo = new ItemVO();
                vo.setName(rs.getString(1));
                vo.setPrice(rs.getFloat(2));
                return vo;
            }
        });
    }

    public List<ItemVO> list(int sizePerPage, int pageNum) {
        String sql = "select name,price,id from item where deleted=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * sizePerPage, sizePerPage}, new RowMapper<ItemVO>() {
            @Override
            public ItemVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ItemVO vo = new ItemVO();
                vo.setName(rs.getString(1));
                vo.setPrice(rs.getFloat(2));
                vo.setId(rs.getInt(3));
                return vo;
            }
        });
    }
}
