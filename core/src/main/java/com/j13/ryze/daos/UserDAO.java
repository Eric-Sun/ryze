package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate j;


    public String getNickName(int userId) {
        String sql = "select nickname from user where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, String.class);
    }






}
