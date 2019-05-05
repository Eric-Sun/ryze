package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.UserLockVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class UserLockDAO {

    @Autowired
    JdbcTemplate j;

    public int lock(final int userId, final int lockOperatorType, final int lockReasonType, final String lockReason, final long locktime, final long
            unlocktime) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user_lock (user_id,lock_reason,locktime,unlocktime,final_unlocktime,lock_operator_type,lock_reason_type) " +
                "values(?,?,?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, lockReason);
                pstmt.setTimestamp(3, new Timestamp(locktime));
                pstmt.setTimestamp(4, new Timestamp(unlocktime));
                pstmt.setTimestamp(5, new Timestamp(unlocktime));
                pstmt.setInt(6, lockOperatorType);
                pstmt.setInt(7, lockReasonType);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public UserLockVO get(int userId) {
        String sql = "select user_id,lock_reason,unlock_reason,locktime,unlocktime,lock_operator_type,lock_reason_type,unlock_reason_type,unlock_operator_type" +
                " from user_lock where user_id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, new RowMapper<UserLockVO>() {
            @Override
            public UserLockVO mapRow(ResultSet resultSet, int i) throws SQLException {
                UserLockVO vo = new UserLockVO();
                vo.setUserId(resultSet.getInt(1));
                vo.setLockReason(resultSet.getString(2));
                vo.setUnlockReason(resultSet.getString(3));
                vo.setLocktime(resultSet.getTimestamp(4).getTime());
                vo.setUnlocktime(resultSet.getTimestamp(5).getTime());
                vo.setLockOperatorType(resultSet.getInt(6));
                vo.setLockType(resultSet.getInt(7));
                vo.setUnlockType(resultSet.getInt(8));
                vo.setUnlockOperatorType(resultSet.getInt(9));
                return vo;
            }
        });
    }

    public void unlock(int userId, int unlockReasonType, int unlockOperatorType, String unlockReason) {
        String sql = "update user_lock set unlock_reason=?,final_unlocktime=now(),unlock_reason_type=?,unlock_operator_type=? where user_id=? and deleted=?";
        j.update(sql, new Object[]{unlockReason, unlockReasonType, unlockOperatorType, userId, Constants.DB.NOT_DELETED});
    }
}
