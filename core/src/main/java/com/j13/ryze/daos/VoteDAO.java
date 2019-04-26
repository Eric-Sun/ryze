package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.destiny.DestinyConstants;
import com.j13.ryze.vos.VoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class VoteDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int resourceId, final int type, final String evidence, final long triggerTime) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into vote " +
                "(user_id,resource_id,type,evidence,createtime,updatetime,triggertime) " +
                "values" +
                "(?,?,?,?,now(),now(),?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, resourceId);
                pstmt.setInt(3, type);
                pstmt.setString(4, evidence);
                pstmt.setTimestamp(5, new Timestamp(triggerTime));
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void agree(int voteId) {
        String sql = "update vote set agree_count=agree_count+1 where id=? and deleted=?";
        j.update(sql, new Object[]{voteId, Constants.DB.NOT_DELETED});
    }

    public void disagree(int voteId) {
        String sql = "update vote set disagree_count=disagree_count+1 where id=? and deleted=?";
        j.update(sql, new Object[]{voteId, Constants.DB.NOT_DELETED});
    }


    public void setResult(int voteId, int result) {
        String sql = "update vote set result=?,status=? where id=? and deleted=?";
        j.update(sql, new Object[]{result, DestinyConstants.Vote.Status.END, voteId, Constants.DB.NOT_DELETED});
    }


    /**
     * 获得正反双方的票数
     *
     * @param voteId
     * @return
     */
    public VoteVO getCount(int voteId) {
        String sql = "select agree_count,disagree_count from vote where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{voteId, Constants.DB.NOT_DELETED}, new RowMapper<VoteVO>() {
            @Override
            public VoteVO mapRow(ResultSet resultSet, int i) throws SQLException {
                VoteVO vo = new VoteVO();
                vo.setAgreeCount(resultSet.getInt(1));
                vo.setDisagreeCount(resultSet.getInt(2));
                return vo;
            }
        });
    }

    public List<VoteVO> list(int pageNum, int size) {
        String sql = "select id,user_id,resource_id,type,evidence,createtime,status from vote where deleted=? order by createtime desc limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<VoteVO>() {
            @Override
            public VoteVO mapRow(ResultSet resultSet, int i) throws SQLException {
                VoteVO vo = new VoteVO();
                vo.setId(resultSet.getInt(1));
                vo.setUserId(resultSet.getInt(2));
                vo.setResourceId(resultSet.getInt(3));
                vo.setType(resultSet.getInt(4));
                vo.setEvidence(resultSet.getString(5));
                vo.setCreatetime(resultSet.getTimestamp(6).getTime());
                vo.setStatus(resultSet.getInt(7));
                return vo;
            }
        });
    }

    public List<VoteVO> getDeadJobs() {
        String sql = "select id,user_id,resource_id,type,status,triggertime from vote where status=? and deleted=?";
        return j.query(sql, new Object[]{DestinyConstants.Vote.Status.START, Constants.DB.NOT_DELETED}, new RowMapper<VoteVO>() {
            @Override
            public VoteVO mapRow(ResultSet resultSet, int i) throws SQLException {
                VoteVO vo = new VoteVO();
                vo.setId(resultSet.getInt(1));
                vo.setUserId(resultSet.getInt(2));
                vo.setResourceId(resultSet.getInt(3));
                vo.setType(resultSet.getInt(4));
                vo.setStatus(resultSet.getInt(5));
                vo.setTriggertime(resultSet.getTimestamp(6).getTime());
                return vo;
            }
        });
    }
}
