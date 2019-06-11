package com.j13.ryze.daos;

import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.UserVO;
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
public class UserDAO {

    @Autowired
    JdbcTemplate j;


//    public String getNickName(int userId) {
//        String sql = "select nickname from user where id=? and deleted=?";
//        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, String.class);
//    }

    public UserVO getUser(int userId) {
        String sql = "select nickname,avatar_img_id,createtime,anon_nickname,is_lock from user where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{userId, Constants.DB.NOT_DELETED}, new RowMapper<UserVO>() {
            @Override
            public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserVO vo = new UserVO();
                vo.setNickName(rs.getString(1));
                vo.setAvatarImgId(rs.getInt(2));
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                vo.setAnonNickName(rs.getString(4));
                vo.setIsLock(rs.getInt(5));
                return vo;
            }
        });
    }

    public List<UserVO> list(int pageNum, int size) {
        String sql = "select nickname,avatar_img_id,createtime,anon_nickname,is_lock,source_type,id from user where deleted=? limit ?,?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<UserVO>() {
            @Override
            public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserVO vo = new UserVO();
                vo.setNickName(rs.getString(1));
                vo.setAvatarImgId(rs.getInt(2));
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                vo.setAnonNickName(rs.getString(4));
                vo.setIsLock(rs.getInt(5));
                vo.setSourceType(rs.getInt(6));
                vo.setUserId(rs.getInt(7));
                return vo;
            }
        });
    }


    public int register(final String nickName, final String anonNickName,
                        final int avatarImgId, final int sourceType) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into user(nickname,anon_nickname,avatar_img_id,source_type,createtime,updatetime) " +
                "values(?,?,?,?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, nickName);
                pstmt.setString(2, anonNickName);
                pstmt.setInt(3, avatarImgId);
                pstmt.setInt(4, sourceType);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public void updateFromWechat(int userId, String nickName, int avatarImgId) {
        String sql = "update user set nickname=? , avatar_img_id=? where id=? and deleted=?";
        j.update(sql, new Object[]{nickName, avatarImgId, userId, Constants.DB.NOT_DELETED});
    }

    public void updateFromWechat(int userId, String nickName) {
        String sql = "update user set nickname=?  where id=? and deleted=?";
        j.update(sql, new Object[]{nickName, userId, Constants.DB.NOT_DELETED});
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


    public void unlockUser(int userId) {
        String sql = "update user set is_lock=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.User.Lock.NO_LOCK, userId, Constants.DB.NOT_DELETED});
    }

    public void lockUser(int userId) {
        String sql = "update user set is_lock=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.User.Lock.IS_LOCK, userId, Constants.DB.NOT_DELETED});
    }

    public List<UserVO> search(String text, int pageNum, int size) {
        String sql = "select nickname,avatar_img_id,createtime,anon_nickname,is_lock,source_type,id from user where nickname like ? and deleted=? limit ?,?";
        return j.query(sql, new Object[]{"%" + text + "%", Constants.DB.NOT_DELETED, pageNum * size, size}, new RowMapper<UserVO>() {
            @Override
            public UserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                UserVO vo = new UserVO();
                vo.setNickName(rs.getString(1));
                vo.setAvatarImgId(rs.getInt(2));
                vo.setCreatetime(rs.getTimestamp(3).getTime());
                vo.setAnonNickName(rs.getString(4));
                vo.setIsLock(rs.getInt(5));
                vo.setSourceType(rs.getInt(6));
                vo.setUserId(rs.getInt(7));
                return vo;
            }
        });
    }


    /**
     * 查询nickname是否有重复情况
     *
     * @param nickName
     * @return
     */
    public boolean checkNickNameExisted(String nickName) {
        String sql = "select count(1) from user where nickname=? ";
        int count = j.queryForObject(sql, new Object[]{nickName}, Integer.class);
        if (count == 0)
            return false;
        else
            return true;
    }

    /**
     * 获取所有机器人用户
     * @return
     */
    public List<Integer> getAllMachineUser() {
        String sql = "select id from user where source_type=?";
        return j.query(sql, new Object[]{Constants.USER_SOURCE_TYPE.MACHINE}, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
                Integer userId = resultSet.getInt(1);
                return userId;
            }
        });
    }
}
