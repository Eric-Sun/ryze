package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BannerBannerPlanVO;
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
public class BannerBannerPlanDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int bannerId, final int bannerPlanId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into banner_banner_plan " +
                "(banner_id,banner_plan_id,createtime) " +
                "values" +
                "(?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, bannerId);
                pstmt.setInt(2, bannerPlanId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    private void delete(int id) {
        String sql = "update banner_banner_plan set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }


}
