package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.TopicVO;
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
public class TopicDAO {
    @Autowired
    JdbcTemplate j;


    public int insert(final String topicName) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into topic (name,createtime,updatetime)" +
                " values (?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, topicName);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public TopicVO get(int topicId){
        String sql = "select id,name,createtime from topic where deleted=? order by id desc ";
        return j.queryForObject(sql, new Object[]{Constants.DB.NOT_DELETED}, new RowMapper<TopicVO>() {
            @Override
            public TopicVO mapRow(ResultSet resultSet, int i) throws SQLException {
                TopicVO vo = new TopicVO();
                vo.setId(resultSet.getInt(1));
                vo.setName(resultSet.getString(2));
                vo.setCreatetime(resultSet.getTimestamp(3).getTime());
                return vo;
            }
        });
    }

    public void updateName(int topicId, String topicName) {
        String sql = "update topic set name=? where id=? ";
        j.update(sql, new Object[]{topicName, topicId});
    }

    public void deleteTopic(int topicId) {
        String sql = "update topic set deleted=? where id=?";
        j.update(sql, new Object[]{Constants.DB.NOT_DELETED}, topicId);
    }

    public List<TopicVO> listTopic(){
        String sql = "select id,name,createtime from topic where deleted=? order by id desc ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED}, new RowMapper<TopicVO>() {
            @Override
            public TopicVO mapRow(ResultSet resultSet, int i) throws SQLException {
                TopicVO vo = new TopicVO();
                vo.setId(resultSet.getInt(1));
                vo.setName(resultSet.getString(2));
                vo.setCreatetime(resultSet.getTimestamp(3).getTime());
                return vo;
            }
        });
    }

}
