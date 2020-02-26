package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ThirdPartInfoDAO {

    @Autowired
    JdbcTemplate j;

    public void updateSessionKey(String openId, String newSessionKey, int thirdPartSource) {
        String sql = "update third_part_info set session_key=? where open_id=? and deleted=? and third_part_source";
        j.update(sql, new Object[]{newSessionKey, openId, Constants.DB.NOT_DELETED, thirdPartSource});
    }


    public int insert(final int userId, final String openId, final String sessionKey, final int thirdPartSource) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into third_part_info (user_id,open_id,session_key,createtime,updatetime,third_part_source)" +
                " values (?,?,?,now(),now(),?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, openId);
                pstmt.setString(3, sessionKey);
                pstmt.setInt(4, thirdPartSource);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public int getUserId(String openId, int thirdPartSource) {
        String sql = "select user_id from third_part_info where open_id=? and deleted=? and third_part_source";
        try {
            return j.queryForObject(sql, new Object[]{openId, Constants.DB.NOT_DELETED, thirdPartSource}, Integer.class);
        } catch (DataAccessException e) {
            return -1;
        }
    }

}

