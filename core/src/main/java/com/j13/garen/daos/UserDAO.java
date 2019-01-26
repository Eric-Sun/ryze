package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.UserVO;
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

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 14-3-19
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class UserDAO {

    @Autowired
    JdbcTemplate j;


    public UserVO loginByMobile(String mobile, String password) {
        final String sql = "select id from user where mobile=? and password=?";
        UserVO vo = j.queryForObject(sql, new Object[]{mobile, password}, new RowMapper<UserVO>() {
            @Override
            public UserVO mapRow(ResultSet resultSet, int i) throws SQLException {
                UserVO vo = new UserVO();
                vo.setUserId(resultSet.getInt(1));
                return vo;
            }
        });
        return vo;

    }


    public long registerFromOwner(final String mobile, final String password, final String nickName, final String avatarUrl) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user(mobile,password,avatar_url,source_type,nick_name,createtime,updatetime) " +
                "values(?,?,?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, mobile);
                pstmt.setString(2, password);
                pstmt.setString(3, avatarUrl);
                pstmt.setInt(4, Constants.USER_SOURCE_TYPE.OWNER);
                pstmt.setString(5, nickName);
                return pstmt;
            }
        }, holder);
        return holder.getKey().longValue();

    }


    public int registerFromWechat(final String nickName, final String avatarUrl) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user(avatar_url,source_type,nick_name,createtime,updatetime) " +
                "values(?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, avatarUrl);
                pstmt.setInt(2, Constants.USER_SOURCE_TYPE.WECHAT);
                pstmt.setString(3, nickName);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void updateFromWechat(int userId, String nickName, String avatarUrl) {
        String sql = "update user set nick_name=? , avatar_url=? where id=? and deleted=?";
        j.update(sql, new Object[]{nickName, avatarUrl, userId, Constants.DB.NOT_DELETED});
    }

    public void updateInfoFromWechat(int userId, String city, String country, String province,
                                     int gender, String language) {
        String sql = "update user_info set city=?,country=?,province=?,gender=?,`language`=? where user_id=? and deleted=?";
        j.update(sql, new Object[]{city, country, province, gender, language, userId, Constants.DB.NOT_DELETED});
    }

    public long registerUserInfoFromWechat(final int userId, final String city, final String country, final String province,
                                           final int gender, final String language) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user_info(user_id,city,country,province,gender,`language`,createtime,updatetime) " +
                "values(?,?,?,?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setString(2, city);
                pstmt.setString(3, country);
                pstmt.setString(4, province);
                pstmt.setInt(5, gender);
                pstmt.setString(6, language);
                return pstmt;
            }
        }, holder);
        return holder.getKey().longValue();
    }

    public boolean mobileExisted(String mobile) {
        String sql = "select count(1) from user where mobile=?";
        int count = j.queryForObject(sql, new Object[]{mobile}, Integer.class);

        return count == 1 ? true : false;

    }

}
