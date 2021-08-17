package com.j13.ryze.daos;

import com.google.common.collect.Lists;
import com.j13.ryze.core.Constants;
import com.j13.ryze.vos.BarVO;
import com.j13.ryze.vos.PostVO;
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
public class PostDAO {

    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int barId, final String title, final String content,
                   final int anonymous, final int type, final String imgList, final int status) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into post " +
                "(user_id,bar_id,content,reply_count,createtime,updatetime,title,status,anonymous,`type`,img_list,audit_status) " +
                "values" +
                "(?,?,?,0,now(),now(),?,?,?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, barId);
                pstmt.setString(3, content);
                pstmt.setString(4, title);
                pstmt.setInt(5, status);
                pstmt.setInt(6, anonymous);
                pstmt.setInt(7, type);
                pstmt.setString(8, imgList);
                pstmt.setInt(9, Constants.POST_AUDIT_STATUS.AUDITING);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }
    public int add(final int userId, final int barId, final String title, final String content,
                   final int anonymous, final int type, final String imgList, final int status,final int auditStatus) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into post " +
                "(user_id,bar_id,content,reply_count,createtime,updatetime,title,status,anonymous,`type`,img_list,audit_status) " +
                "values" +
                "(?,?,?,0,now(),now(),?,?,?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, barId);
                pstmt.setString(3, content);
                pstmt.setString(4, title);
                pstmt.setInt(5, status);
                pstmt.setInt(6, anonymous);
                pstmt.setInt(7, type);
                pstmt.setString(8, imgList);
                pstmt.setInt(9, auditStatus);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }

    public int add(final int userId, final int barId, final String title, final String content,
                   final int anonymous, final int type, final String imgList) {
        return add(userId, barId, title, content, anonymous, type, imgList, Constants.POST_STATUS.ONLINE);
    }

    public int addOffline(final int userId, final int barId, final String title, final String content,
                          final int anonymous, final int type, final String imgList,final int auditStatus) {
        return add(userId, barId, title, content, anonymous, type, imgList, Constants.POST_STATUS.OFFLINE,auditStatus);
    }


    public List<Integer> list(int barId, int pageNum, int size) {
        String sql = "select id " +
                "from post where deleted=? and bar_id=? and status=? and audit_status=? order by updatetime desc limit ?,?";
        return j.queryForList(sql, new Object[]{Constants.DB.NOT_DELETED, barId, Constants.POST_STATUS.ONLINE, Constants.POST_AUDIT_STATUS.NORMAL, pageNum * size, size},
                Integer.class);
    }

    public List<Integer> listByType(int barId, int type, int pageNum, int size) {
        String sql = "select id " +
                "from post where deleted=? and bar_id=? and status=? and `type`=? and audit_stauts=? order by updatetime desc limit ?,?";
        return j.queryForList(sql, new Object[]{Constants.DB.NOT_DELETED, barId, Constants.POST_STATUS.ONLINE, type, Constants.POST_AUDIT_STATUS.NORMAL, Constants.POST_AUDIT_STATUS.NORMAL,
                pageNum * size, size}, Integer.class);
    }


    public void update(int postId, String content, String title, int anonymous, int type, String imgListStr) {
        String sql = "update post set content=?,title=?,anonymous=?,`type`=?,img_list=? where id=? and deleted=?";
        j.update(sql, new Object[]{content, title, anonymous, type, imgListStr, postId, Constants.DB.NOT_DELETED});
    }

    public void delete(int postId) {
        String sql = "update post set deleted=? where id=? and deleted=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, postId, Constants.DB.NOT_DELETED});
    }

    public void updateAuditStatus(int postId, int auditStatus) {
        String sql = "update post set audit_status=? where id=? ";
        j.update(sql, new Object[]{auditStatus, postId});
    }

    public PostVO get(int postId) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "reply_count,updatetime,title,status,anonymous,`type`,img_list,audit_status  from post where id=? ";
        return j.queryForObject(sql, new Object[]{postId}, new PostRowMapper());
    }

    public void addReplyCount(int postId) {
        String sql = "update post set reply_count=reply_count+1 where id=?";
        j.update(sql, new Object[]{postId});
    }

    public void reduceReplyCount(int postId) {
        String sql = "update post set reply_count=reply_count-1 where id=?";
        j.update(sql, new Object[]{postId});
    }

    public void updateTime(int postId) {
        String sql = "update post set updatetime=now() where id=?";
        j.update(sql, new Object[]{postId});
    }

    public void offline(int postId) {
        String sql = "update post set status=? where id=?";
        j.update(sql, new Object[]{Constants.POST_STATUS.OFFLINE, postId});
    }

    public void online(int postId) {
        String sql = "update post set status=? where id=?";
        j.update(sql, new Object[]{Constants.POST_STATUS.ONLINE, postId});
    }

    public List<PostVO> queryByTtile(int barId, String name, int pageNum, int size) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "reply_count,updatetime,title,status,anonymous,`type`,img_list, " +
                "audit_status from post where title like ? and bar_id = ? and deleted=? and audit_status=?  order by updatetime desc  limit ?,?";
        return j.query(sql, new Object[]{"%" + name + "%", barId, Constants.DB.NOT_DELETED, Constants.POST_AUDIT_STATUS.NORMAL, pageNum * size, size}, new PostRowMapper());
    }

    public List<Integer> unauditList(int barId, int pageNum, int size) {
        String sql = "select id from post where audit_status=? and bar_id = ? and deleted=?  order by createtime desc  limit ?,?";
        return j.queryForList(sql, new Object[]{Constants.POST_AUDIT_STATUS.AUDITING, barId, Constants.DB.NOT_DELETED, pageNum * size, size}, Integer.class);
    }


    public List<PostVO> queryByUserId(int barId, int userId, int pageNum, int size) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "reply_count,updatetime,title,status,anonymous,`type`,img_list,audit_status " +
                "from post where user_id=? and bar_id = ? and deleted=? and audit_status=? order by updatetime desc limit ?,?";
        return j.query(sql, new Object[]{userId, barId, Constants.DB.NOT_DELETED, Constants.POST_AUDIT_STATUS.NORMAL, pageNum * size, size}, new PostRowMapper());
    }

    public List<PostVO> listByUserId(int barId, int userId, int pageNum, int size) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "reply_count,updatetime,title,status,anonymous,`type`,img_list,audit_status " +
                "from post where user_id=? and deleted=? and bar_id=? order by updatetime desc limit ?,?";
        return j.query(sql, new Object[]{userId, Constants.DB.NOT_DELETED, barId, pageNum * size, size}, new PostRowMapper());
    }

    public void delete(int postId, int userId) {
        String sql = "update post set deleted=? where id=? and user_id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, postId, userId});
    }

    public List<PostVO> recentlyOtherUserPostList(int otherUserId, int barId, int pageNum, int size) {
        String sql = "select user_id,bar_id,content,createtime,id," +
                "reply_count,updatetime,title,status,anonymous,`type`,img_list,audit_status " +
                "from post where user_id=? and deleted=? and bar_id=? and anonymous=? and status=? order by updatetime desc limit ?,?";
        return j.query(sql, new Object[]{otherUserId, Constants.DB.NOT_DELETED, barId,
                Constants.POST_ANONYMOUS.COMMON, Constants.POST_STATUS.ONLINE, pageNum * size, size}, new PostRowMapper());
    }

    public List<Integer> offlineList(int barId, int pageNum, int size) {
        String sql = "select id from post where status=? and deleted=? and bar_id=? and audit_status=? order by createtime desc limit ?,?";
        return j.queryForList(sql, new Object[]{Constants.POST_STATUS.OFFLINE, Constants.DB.NOT_DELETED, barId, Constants.POST_AUDIT_STATUS.NORMAL, pageNum * size, size}, Integer.class);
    }

    public int postCount(int barId) {
        String sql = "select count(1) from post where bar_id=? and audit_status=? and status =? and deleted=?";
        return j.queryForObject(sql, new Object[]{barId, Constants.POST_AUDIT_STATUS.NORMAL, Constants.POST_STATUS.ONLINE, Constants.DB.NOT_DELETED}, Integer.class);
    }

    public List<Integer> deletedList(int barId, int pageNum, int size) {
        String sql = "select id from post where deleted=? and bar_id=? and audit_status=? order by updatetime desc limit ?,? ";
        return j.queryForList(sql, new Object[]{Constants.DB.DELETED, barId, Constants.POST_AUDIT_STATUS.NORMAL, pageNum * size, size}, Integer.class);
    }

    public int deletedListCount(int barId) {
        String sql = "select count(1) from post where deleted=? and audit_status=? and bar_id=? ";
        return j.queryForObject(sql, new Object[]{Constants.DB.DELETED, Constants.POST_AUDIT_STATUS.NORMAL, barId}, Integer.class);

    }

    public void undoDelete(int barId, int postId) {
        String sql = "update post set deleted=? where bar_id=? and id=?";
        j.update(sql, new Object[]{Constants.DB.NOT_DELETED, barId, postId});

    }

    public int offlineListCount(int barId) {
        String sql = "select count(1) from post where status=? and deleted=? and bar_id=? and audit_status=? ";
        return j.queryForObject(sql, new Object[]{Constants.POST_STATUS.OFFLINE, Constants.DB.NOT_DELETED, barId, Constants.POST_AUDIT_STATUS.NORMAL}, Integer.class);
    }

    public void updateImg(int postId, String imgIdListStr) {
        String sql = "update post set img_list=? where id=?";
        j.update(sql, new Object[]{imgIdListStr, postId});
    }

    /**
     * 为后台提供的通用插入逻辑
     *
     * @param barId
     * @param postId
     * @param title
     * @param userId
     * @param pageNum
     * @param size
     * @return
     */
    public List<Integer> query(int barId, int postId, String title, int userId, int pageNum, int size) {
        String postStr = "";
        String titleStr = "";
        String userIdStr = "";
        List<Object> paramList = Lists.newLinkedList();
        paramList.add(barId);
        paramList.add(Constants.DB.NOT_DELETED);
        // 判断是否有postId
        if (postId != 0) {
            postStr = "and id=? ";
            paramList.add(postId);
        }
        if (!title.equals("")) {
            titleStr = " and title like ? ";
            paramList.add("%" + title + "%");
        }
        if (userId != 0) {
            userIdStr = " and user_id=? ";
            paramList.add(userId);
        }
        paramList.add(pageNum * size);
        paramList.add(size);
        String sql = "select id from post where bar_id=? and deleted=? " + postStr + titleStr + userIdStr + " and audit_status=0  order by id desc limit ?,?";
        return j.queryForList(sql, paramList.toArray(), Integer.class);
    }


    class PostRowMapper implements RowMapper<PostVO> {
        @Override
        public PostVO mapRow(ResultSet rs, int i) throws SQLException {
            PostVO vo = new PostVO();
            vo.setUserId(rs.getInt(1));
            vo.setBarId(rs.getInt(2));
            vo.setContent(rs.getString(3));
            vo.setCreatetime(rs.getTimestamp(4).getTime());
            vo.setPostId(rs.getInt(5));
            vo.setReplyCount(rs.getInt(6));
            vo.setUpdatetime(rs.getTimestamp(7).getTime());
            vo.setTitle(rs.getString(8));
            vo.setStatus(rs.getInt(9));
            vo.setAnonymous(rs.getInt(10));
            vo.setType(rs.getInt(11));
            vo.setImgListStr(rs.getString(12));
            vo.setAuditStatus(rs.getInt(13));
            return vo;
        }
    }

    public List<Integer> listPostIdForCache() {
        String sql = "select id from post where status=? and deleted=? and audit_status=?";
        return j.queryForList(sql, new Object[]{Constants.POST_STATUS.ONLINE, Constants.DB.NOT_DELETED, Constants.POST_AUDIT_STATUS.NORMAL}, Integer.class);
    }
}
