package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.RoomChatContentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class RoomChatDAO {
    @Autowired
    JdbcTemplate j;

    public int createRoom(final int userId, final String passwordAfterMD5) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into chat_room (user_id,password,createtime) values (?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, passwordAfterMD5);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public int getRoomByPassword(String passwordAfterMD5) {
        try {
            final String sql = "select id from chat_room where password=? and deleted=?";
            return j.queryForObject(sql, new Object[]{passwordAfterMD5, Constants.DB.NOT_DELETED}, Integer.class);
        } catch (DataAccessException e) {
            return 0;
        }

    }

    public int addMember(final int crId, final int userId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into chat_room_member (cr_id,user_id,createtime) values (?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, crId);
                pstmt.setInt(2, userId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public boolean checkMember(int crId, int userId) {
        String sql = "select count(1) from chat_room_member where cr_id=? and user_id=? and deleted=?";
        int count = j.queryForObject(sql, new Object[]{crId, userId, Constants.DB.NOT_DELETED}, Integer.class);
        return count == 1 ? true : false;
    }


    public int sendContent(final int userId, final int crId, final String content) {

        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into chat_room_content (cr_id,from_user_id,content,createtime) values (?,?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, crId);
                pstmt.setInt(2, userId);
                pstmt.setString(3, content);
                return pstmt;
            }
        }, holder);

        return holder.getKey().intValue();
    }


    public List<RoomChatContentVO> loadContent(int crId, int count) {
        String sql = "select from_user_id,content,createtime,id from chat_room_content where cr_id=? and deleted=? order by createtime DESC " +
                "limit 0,?";
        return j.query(sql, new Object[]{crId, Constants.DB.NOT_DELETED, count}, new RowMapper<RoomChatContentVO>() {
            @Override
            public RoomChatContentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RoomChatContentVO vo = new RoomChatContentVO();
                vo.setFromUserId(rs.getInt(1));
                vo.setContent(rs.getString(2));
                vo.setTime(rs.getTimestamp(3).getTime());
                vo.setId(rs.getInt(4));
                return vo;
            }
        });
    }

    public List<RoomChatContentVO> loadContentByIndexToDown(int crId, int index) {
        String sql = "select from_user_id,content,createtime,id from chat_room_content " +
                "where cr_id=? and deleted=? and id>? order by createtime DESC " +
                "";
        return j.query(sql, new Object[]{crId, Constants.DB.NOT_DELETED, index}, new RowMapper<RoomChatContentVO>() {
            @Override
            public RoomChatContentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RoomChatContentVO vo = new RoomChatContentVO();
                vo.setFromUserId(rs.getInt(1));
                vo.setContent(rs.getString(2));
                vo.setTime(rs.getTimestamp(3).getTime());
                vo.setId(rs.getInt(4));
                return vo;
            }
        });
    }

    public List<RoomChatContentVO> loadContentByIndexToUp(int crId, int index, int limit) {
        String sql = "select from_user_id,content,createtime,id from chat_room_content " +
                "where cr_id=? and deleted=? and id<? order by createtime DESC limit 0,?" +
                "";
        return j.query(sql, new Object[]{crId, Constants.DB.NOT_DELETED, index, limit}, new RowMapper<RoomChatContentVO>() {
            @Override
            public RoomChatContentVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                RoomChatContentVO vo = new RoomChatContentVO();
                vo.setFromUserId(rs.getInt(1));
                vo.setContent(rs.getString(2));
                vo.setTime(rs.getTimestamp(3).getTime());
                vo.setId(rs.getInt(4));
                return vo;
            }
        });
    }


}
