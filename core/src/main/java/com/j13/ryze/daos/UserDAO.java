package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate j;


    public String getNickName(int userId) {
        String sql = "select nickname from user where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, String.class);
    }


    public int add(final String nickName, final String anonNickName, final int sex, final int isMachine) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user (nickname,anon_nickname,sex,is_machine,createtime,updatetime)" +
                "values (?,?,?,?,now(),now()) ";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, nickName);
                pstmt.setString(2, anonNickName);
                pstmt.setInt(3, sex);
                pstmt.setInt(4, isMachine);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }




}
