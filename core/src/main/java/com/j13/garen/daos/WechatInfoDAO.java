package com.j13.garen.daos;

import com.j13.garen.core.Constants;
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
public class WechatInfoDAO {

    @Autowired
    JdbcTemplate j;

    public void updateSessionKey(String openId, String newSessionKey) {
        String sql = "update wechat_info set session_key=? where open_id=? and deleted=?";
        j.update(sql, new Object[]{newSessionKey, openId, Constants.DB.NOT_DELETED});
    }


    public int insert(final int userId, final String openId, final String sessionKey) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into wechat_info (user_id,open_id,session_key,createtime,updatetime)" +
                " values (?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, openId);
                pstmt.setString(3, sessionKey);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public int getUserId(String openId) {
        String sql = "select user_id from wechat_info where open_id=? and deleted=?";
        try {
            return j.queryForObject(sql, new Object[]{openId, Constants.DB.NOT_DELETED}, Integer.class);
        } catch (DataAccessException e) {
            return -1;
        }
    }


}

