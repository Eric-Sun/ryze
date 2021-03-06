package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.ImgVO;
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

    public String getName(int imgId) {
        String sql = "select name from img where id=?";
        return j.queryForObject(sql, new Object[]{imgId}, String.class);
    }

    public void delete(int imgId) {
        String sql = "update img set deleted=?,updatetime=now() where id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, imgId});
    }

    public List<ImgVO> listForAvatar() {
        String sql = "select id,name,type from img where type=? and deleted=?";
        return j.query(sql, new Object[]{Constants.IMG_TYPE.AVATAR, Constants.DB.NOT_DELETED}, new RowMapper<ImgVO>() {
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
