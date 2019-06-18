package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.CollectVO;
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
public class CollectDAO {
    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int type, final int resourceId) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into collect " +
                "(user_id,type,resource_id,createtime) values " +
                "(?,?,?,now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, type);
                pstmt.setInt(3, resourceId);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void delete(int userId, int type, int postId) {
        String sql = "update collect set deleted=? where type=? and resource_id=? and user_id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, type, postId, userId, Constants.DB.NOT_DELETED});
    }

    public List<CollectVO> list(final int userId, int pageNum, int size) {
        String sql = "select id,type,resource_id,createtime from collect where user_id=? and deleted=? order by createtime desc limit ?,?";
        return j.query(sql, new Object[]{userId, Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<CollectVO>() {
            @Override
            public CollectVO mapRow(ResultSet resultSet, int i) throws SQLException {
                CollectVO vo = new CollectVO();
                vo.setId(resultSet.getInt(1));
                vo.setUserId(userId);
                vo.setType(resultSet.getInt(2));
                vo.setResourceId(resultSet.getInt(3));
                vo.setCreatetime(resultSet.getTimestamp(4).getTime());
                return vo;
            }
        });
    }


    public boolean checkExist(int userId, int type, int postId) {
        String sql = "select count(1) from collect where user_id=? and deleted=? and type=? and resource_id=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED, type, postId}, Integer.class) == 0 ? false : true;
    }
}
