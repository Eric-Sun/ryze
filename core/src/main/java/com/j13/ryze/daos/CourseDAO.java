package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.course.CourseInfo;
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
public class CourseDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final String name, final int type, final String data, final String tips1,
                   final String tips2, final float price, final float discountedPrice) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into course " +
                "(name,type,data,createtime,tips1,tips2,price,discounted_price) values " +
                "(?,?,?,now(),?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setInt(2, type);
                pstmt.setString(3, data);
                pstmt.setString(4, tips1);
                pstmt.setString(5, tips2);
                pstmt.setFloat(6, price);
                pstmt.setFloat(7, discountedPrice);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void delete(int id) {
        String sql = "update course set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }

    public CourseInfo get(final int id) {
        String sql = "select name,type,data,status,createtime,tips1,tips2,price,discounted_price from course where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{id, Constants.DB.NOT_DELETED}, new RowMapper<CourseInfo>() {

            @Override
            public CourseInfo mapRow(ResultSet rs, int i) throws SQLException {
                CourseInfo ci = new CourseInfo();
                ci.setId(id);
                ci.setName(rs.getString(1));
                ci.setType(rs.getInt(2));
                ci.setData(rs.getString(3));
                ci.setStatus(rs.getInt(4));
                ci.setTips1(rs.getString(5));
                ci.setTips2(rs.getString(6));
                ci.setPrice(rs.getFloat(7));
                ci.setDiscountedPrice(rs.getFloat(8));
                return ci;
            }
        });
    }


}
