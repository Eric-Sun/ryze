package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BannerVO;
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
public class BannerDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final String name, final int urlImgId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into banner " +
                "(name,url_img_id,createtime) " +
                "values" +
                "(?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setInt(2, urlImgId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void delete(int id) {
        String sql = "update banner set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }


    public void update(int id, String name, int urlImgId) {
        String sql = "update banner set name=?,url_img_id=? where id=?";
        j.update(sql, new Object[]{name, urlImgId, id});
    }

    public List<BannerVO> list(int size, int pageNum){
        String sql = "select id,name,url_img_id,createtime from banner where deleted=? order by id desc limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<BannerVO>() {
            @Override
            public BannerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BannerVO vo = new BannerVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setUrlImgId(rs.getInt(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                return vo;
            }
        });
    }

}
