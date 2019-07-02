package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class NoticeDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final int fromUserId, final int toUserId,
                   final int targetResourceId, final int replyId, final int type, final int status) {
        KeyHolder holder = new GeneratedKeyHolder();

        final String sql = "insert into notice (from_user_id,to_user_id,target_resource_id," +
                "reply_id,type,status,createtime,updatetime)" +
                " values (?,?,?,?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, fromUserId);
                pstmt.setInt(2, toUserId);
                pstmt.setInt(3, targetResourceId);
                pstmt.setInt(4, replyId);
                pstmt.setInt(5, type);
                pstmt.setInt(6, status);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    /**
     * 修改通知状态
     *
     * @param userId
     * @param noticeId
     * @param status
     */
    public void updateStatus(int userId, int noticeId, int status) {
        String sql = "update notice set status=?,updatetime=now() where to_user_id=? and deleted=? and id=?";
        j.update(sql, new Object[]{status, userId, Constants.DB.NOT_DELETED, noticeId});
    }


    public void delete(int userId, int noticeId) {
        String sql = "update notice set deleted=?,updatetime=now() where id=? and to_user_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, noticeId, userId});
    }

    public List<NoticeVO> list(int userId) {
        String sql = "select id,from_user_id,to_user_id,target_resource_id,reply_id,type,status,createtime" +
                " from notice " +
                "where deleted=? and to_user_id=? order by updatetime desc";

        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, userId}, new RowMapper<NoticeVO>() {
            @Override
            public NoticeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                NoticeVO vo = new NoticeVO();
                vo.setNoticeId(rs.getInt(1));
                vo.setFromUserId(rs.getInt(2));
                vo.setToUserId(rs.getInt(3));
                vo.setTargetResourceId(rs.getInt(4));
                vo.setReplyId(rs.getInt(5));
                vo.setType(rs.getInt(6));
                vo.setStatus(rs.getInt(7));
                vo.setCreatetime(rs.getTimestamp(8).getTime());
                return vo;
            }
        });
    }

    public int listNotReadSize(int userId) {
        String sql = "select count(1)" +
                " from notice " +
                "where deleted=? and to_user_id=? and status=? ";
        return j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED, userId, Constants.NOTICE.STATUS.NOT_READ}, Integer.class);
    }

    public void readAll(int uid) {
        String sql = "update notice set status=? where to_user_id=? and deleted=?";
        j.update(sql, new Object[]{Constants.NOTICE.STATUS.READED, uid, Constants.DB.NOT_DELETED});
    }

    public int getPostCollectionNoticeId(int userId, int postId) {
        String sql = "select id from notice where to_user_id=? and target_resource_id=? and status=? and deleted=? and type=?";
        int noticeId = 0;
        try {
            noticeId = j.queryForObject(sql, new Object[]{userId, postId, Constants.NOTICE.STATUS.NOT_READ,
                    Constants.DB.NOT_DELETED, Constants.NOTICE.TYPE.POST_COLLECTION_NEW_INFO}, Integer.class);
        } catch (DataAccessException e) {
            return noticeId;
        }
        return noticeId;
    }

    public void updateUpdateTime(int noticeId) {
        String sql = "update notice set updatetime=now() where id=? ";
        j.update(sql, new Object[]{noticeId});
    }
}
