package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.BannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BannerDAO {


    @Autowired
    JdbcTemplate j;


    public List<BannerVO> list() {
        String sql = "select id,url from banner where online=? and deleted=? order by seq desc ";
        return j.query(sql, new Object[]{Constants.Banner.ONLINE, Constants.DB.NOT_DELETED}, new RowMapper<BannerVO>() {
            @Override
            public BannerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                BannerVO vo = new BannerVO();
                vo.setId(rs.getInt(1));
                vo.setUrl(rs.getString(2));
                return vo;
            }
        });
    }
}
