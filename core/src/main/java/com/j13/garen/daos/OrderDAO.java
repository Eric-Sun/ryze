package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.OrderVO;
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
public class OrderDAO {


    @Autowired
    JdbcTemplate j;

    public int add(final int userId, final int itemId, final float finalPrice, final int status, final int imgId,
                   final String remark, final String orderNumber) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into `order` " +
                "(user_id,item_id,final_price,status,createtime,updatetime,img_id,remark,order_number,painter_id) " +
                "values" +
                "(?,?,?,?,now(),now(),?,?,?,?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, userId);
                pstmt.setInt(2, itemId);
                pstmt.setFloat(3, finalPrice);
                pstmt.setInt(4, status);
                pstmt.setInt(5, imgId);
                pstmt.setString(6, remark);
                pstmt.setString(7, orderNumber);
                pstmt.setInt(8, Constants.Order.NO_PAINTER);
                return pstmt;
            }
        }, holder);
        final int orderId = holder.getKey().intValue();
        return orderId;
    }


    public void delete(String orderNumber) {
        String sql = "update `order` set deleted=?,updatetime=now() where order_number=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, orderNumber});
    }

//    public void updateBasicInfo(int orderId, int itemId, float finalPrice, int imgId, String contactMobile) {
//        String sql = "update `order` set item_id=?,final_price=?,updatetime=now(),img_id=?,contact_mobile=? " +
//                "where id=? and deleted=?";
//        j.update(sql, new Object[]{itemId, finalPrice, imgId, contactMobile, orderId, Constants.DB.NOT_DELETED});
//    }


//    public void updateBasicInfo(int orderId, int itemId, float finalPrice, String contactMobile) {
//        String sql = "update `order` set item_id=?,final_price=?,updatetime=now(),contact_mobile=? " +
//                "where id=? and deleted=?";
//        j.update(sql, new Object[]{itemId, finalPrice, contactMobile, orderId, Constants.DB.NOT_DELETED});
//    }

    public void updateStatus(String orderNumber, int status) {
        String sql = "update `order` set status=?,updatetime=now() where order_number=? and deleted=?";
        j.update(sql, new Object[]{status, orderNumber, Constants.DB.NOT_DELETED});
    }

    public OrderVO get(String orderNumber) {
        String sql = "select o.user_id,o.item_id,o.final_price,o.status,i.name,u.nick_name," +
                "o.createtime,o.img_id,o.id,o.remark,o.order_number,o.painter_id " +
                "from `order` o " +
                "left outer join user u on u.id=o.user_id" +
                " left outer join item i on i.id=o.item_id where o.order_number=? and o.deleted=? and i.deleted=? and u.deleted=?";
        return j.queryForObject(sql, new Object[]{orderNumber, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<OrderVO>() {
            @Override
            public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderVO vo = new OrderVO();
                vo.setUserId(rs.getInt(1));
                vo.setItemId(rs.getInt(2));
                vo.setFinalPrice(rs.getFloat(3));
                vo.setStatus(rs.getInt(4));
                vo.setUserName(rs.getString(5));
                vo.setItemName(rs.getString(6));
                vo.setCreatetime(rs.getTimestamp(7).getTime());
                vo.setImgId(rs.getInt(8));
                vo.setId(rs.getInt(9));
                vo.setRemark(rs.getString(10));
                vo.setOrderNumber(rs.getString(11));
                vo.setPainterId(rs.getInt(12));
                return vo;
            }
        });
    }


    public List<OrderVO> list(int sizePerPage, int pageNum) {
        String sql = "select o.user_id,o.item_id,o.final_price,o.status,i.name,u.nick_name,o.createtime," +
                "o.img_id,o.id,o.remark,o.order_number,o.painter_id " +
                " from `order` o " +
                "left outer join user u on u.id=o.user_id " +
                "left outer join item i on i.id=o.item_id where o.deleted=? and u.deleted=? and i.deleted=? limit ?,? ";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, sizePerPage * pageNum, sizePerPage}, new RowMapper<OrderVO>() {
            @Override
            public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderVO vo = new OrderVO();
                vo.setUserId(rs.getInt(1));
                vo.setItemId(rs.getInt(2));
                vo.setFinalPrice(rs.getFloat(3));
                vo.setStatus(rs.getInt(4));
                vo.setItemName(rs.getString(5));
                vo.setUserName(rs.getString(6));
                vo.setCreatetime(rs.getTimestamp(7).getTime());
                vo.setImgId(rs.getInt(8));
                vo.setId(rs.getInt(9));
                vo.setRemark(rs.getString(10));
                vo.setOrderNumber(rs.getString(11));
                vo.setPainterId(rs.getInt(12));
                return vo;
            }
        });
    }

    public List<OrderVO> list(int sizePerPage, int pageNum, int status) {
        String sql = "select o.user_id,o.item_id,o.final_price,o.status,i.name,u.nick_name,o.createtime,o.img_id,o.id," +
                "o.remark,o.order_number " +
                "from `order` o " +
                "left outer join user u on u.id=o.user_id " +
                "left outer join item i on i.id=o.item_id " +
                "where status=? and o.deleted=? and u.deleted=? and i.deleted=? limit ?,? ";
        return j.query(sql, new Object[]{status, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, sizePerPage * pageNum, sizePerPage}, new RowMapper<OrderVO>() {
            @Override
            public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderVO vo = new OrderVO();
                vo.setUserId(rs.getInt(1));
                vo.setItemId(rs.getInt(2));
                vo.setFinalPrice(rs.getFloat(3));
                vo.setStatus(rs.getInt(4));
                vo.setUserName(rs.getString(5));
                vo.setItemName(rs.getString(6));
                vo.setCreatetime(rs.getTimestamp(7).getTime());
                vo.setImgId(rs.getInt(8));
                vo.setId(rs.getInt(9));
                vo.setRemark(rs.getString(10));
                vo.setOrderNumber(rs.getString(11));
                return vo;
            }
        });
    }


    public void setPainter(String orderNumber, int accountId) {
        String sql = "update `order` set painter_id=? where order_number=? and deleted=?";
        j.update(sql, new Object[]{accountId, orderNumber, Constants.DB.NOT_DELETED});
    }

    public List<OrderVO> listByUserId(int userId) {
        String sql = "select o.user_id,o.item_id,o.final_price,o.status,i.name,u.nick_name,o.createtime,o.img_id,o.id," +
                "o.remark,o.order_number " +
                "from `order` o " +
                "left outer join user u on u.id=o.user_id " +
                "left outer join item i on i.id=o.item_id " +
                "where u.id=? and  o.deleted=? and u.deleted=? and i.deleted=? order by o.createtime desc";
        return j.query(sql, new Object[]{userId, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new RowMapper<OrderVO>() {
            @Override
            public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderVO vo = new OrderVO();
                vo.setUserId(rs.getInt(1));
                vo.setItemId(rs.getInt(2));
                vo.setFinalPrice(rs.getFloat(3));
                vo.setStatus(rs.getInt(4));
                vo.setItemName(rs.getString(5));
                vo.setUserName(rs.getString(6));
                vo.setCreatetime(rs.getTimestamp(7).getTime());
                vo.setImgId(rs.getInt(8));
                vo.setId(rs.getInt(9));
                vo.setRemark(rs.getString(10));
                vo.setOrderNumber(rs.getString(11));
                return vo;
            }
        });
    }
}
