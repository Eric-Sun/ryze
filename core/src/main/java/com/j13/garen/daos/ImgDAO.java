package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.ImgVO;
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
public class ImgDAO {

    @Autowired
    JdbcTemplate j;

    public int insert(final String fileName, final int type) {
        KeyHolder holder = new GeneratedKeyHolder();

        final String sql = "insert into img (name,type,createtime,updatetime) values (?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, fileName);
                pstmt.setInt(2, type);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public ImgVO get(int id) {
        String sql = "select id,name,type from img where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{id, Constants.DB.NOT_DELETED}, new RowMapper<ImgVO>() {
            @Override
            public ImgVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                ImgVO vo = new ImgVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setType(rs.getInt(3));
                return vo;
            }
        });
    }
}
