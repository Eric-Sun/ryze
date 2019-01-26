package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.OrderActionRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrderActionRecordDAO {

    @Autowired
    JdbcTemplate j;


    public void add(final int accountId, final String orderNumber,
                    final String img, final String remark, final int actionType) {
        final String sql = "insert into order_action_record " +
                "(account_id,action_type,img,remark,createtime,updatetime,order_number) " +
                "values" +
                "(?,?,?,?,now(),now(),?)";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, accountId);
                pstmt.setInt(2, actionType);
                pstmt.setString(3, img);
                pstmt.setString(4, remark);
                pstmt.setString(5, orderNumber);
                return pstmt;
            }
        });
    }

    public List<OrderActionRecordVO> list(String orderNumber) {
        String sql = "select account_id,action_type,img,remark,createtime,order_number " +
                "from order_action_record where deleted=? and order_number=?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED, orderNumber}, new RowMapper<OrderActionRecordVO>() {
            @Override
            public OrderActionRecordVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderActionRecordVO vo = new OrderActionRecordVO();
                vo.setAccountId(rs.getInt(1));
                vo.setActionType(rs.getInt(2));
                vo.setImg(rs.getString(3));
                vo.setRemark(rs.getString(4));
                vo.setCreatetime(rs.getDate(5).getTime());
                vo.setOrderNumber(rs.getString(6));
                return vo;
            }
        });
    }

}
