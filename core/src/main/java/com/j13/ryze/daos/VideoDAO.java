package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.VideoVO;
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
public class VideoDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final String name, final int type) {
        KeyHolder holder = new GeneratedKeyHolder();

        final String sql = "insert into video_info (name,type,createtime) values (?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                pstmt.setInt(2, type);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void delete(int id) {
        String sql = "update video set deieted=? where id-?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }

    public VideoVO get(int id){
        String sql = "select id,name,type from video where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{id,Constants.DB.NOT_DELETED}, new RowMapper<VideoVO>() {
            @Override
            public VideoVO mapRow(ResultSet rs, int i) throws SQLException {
                VideoVO vo = new VideoVO();
                vo.setId(rs.getInt(1));
                vo.setName(rs.getString(2));
                vo.setType(rs.getInt(3));
                return vo;
            }
        });
    }

}
