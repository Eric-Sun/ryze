package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BannerBannerPlanVO;
import com.j13.ryze.vos.BannerPlanVO;
import com.j13.ryze.vos.BannerVO;
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
public class BannerPlanDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final String name, final int type) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into banner_plan " +
                "(name,type,createtime) " +
                "values" +
                "(?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setInt(2, type);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void delete(int id) {
        String sql = "update banner_plan set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }


    public void update(int id, String name) {
        String sql = "update banner_plan set name=? where id=?";
        j.update(sql, new Object[]{name, id});
    }

    public List<BannerPlanVO> list(int size, int pageNum){
        String sql = "select id,name,type,createtime from banner_plan where deleted=? order by id desc limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<BannerPlanVO>() {
            @Override
            public BannerPlanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BannerPlanVO vo = new BannerPlanVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setType(rs.getInt(3));
                vo.setCreatetime(rs.getTimestamp(4).getTime());
                return vo;
            }
        });
    }



    public List<BannerBannerPlanVO> listBanners(final int bannerPlanId, int size, int pageNum){
        String sql = "select id,banner_id,createtime from banner_banner_plan where deleted=? and banner_plan_id=? order by id desc limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<BannerBannerPlanVO>() {
            @Override
            public BannerBannerPlanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BannerBannerPlanVO vo = new BannerBannerPlanVO();
                vo.setId(rs.getInt(1));
                vo.setBannerId(rs.getInt(2));
                vo.setBannerPlanId(bannerPlanId);
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                return vo;
            }
        });
    }


}
