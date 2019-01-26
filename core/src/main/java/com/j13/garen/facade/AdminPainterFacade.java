package com.j13.garen.facade;

import com.j13.garen.api.req.AdminPainterOrderGetReq;
import com.j13.garen.api.req.AdminPainterOrderListReq;
import com.j13.garen.api.req.AdminPainterRecordAddReq;
import com.j13.garen.api.req.OrderUpdateStatusReq;
import com.j13.garen.api.resp.*;
import com.j13.garen.core.Constants;
import com.j13.garen.daos.OrderActionRecordDAO;
import com.j13.garen.daos.OrderDAO;
import com.j13.garen.services.ThumbService;
import com.j13.garen.vos.OrderActionRecordVO;
import com.j13.garen.vos.OrderVO;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 画家在后台的操作
 */
@Component
public class AdminPainterFacade {

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    OrderActionRecordDAO orderActionRecordDAO;
    @Autowired
    ThumbService thumbService;


    @Action(name = "admin.painter.order.list", desc = "banner list.")
    public AdminPainterOrderListResp orderList(CommandContext ctxt, AdminPainterOrderListReq req) {
        AdminPainterOrderListResp resp = new AdminPainterOrderListResp();
        List<OrderVO> list = null;
        if (req.getStatus() == Constants.OrderStatus.QUERY_ALL_STATUS) {
            list = orderDAO.list(req.getSizePerPage(), req.getPageNum());
        } else {
            list = orderDAO.list(req.getSizePerPage(), req.getPageNum(), req.getStatus());
        }
        for (OrderVO vo : list) {
            AdminPainterOrderGetResp r = new AdminPainterOrderGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }


    @Action(name = "admin.painter.order.get", desc = "get order detail")
    public AdminPainterOrderGetResp get(CommandContext ctxt, AdminPainterOrderGetReq req) {
        OrderVO orderVO = orderDAO.get(req.getOrderNumber());

        AdminPainterOrderGetResp resp = new AdminPainterOrderGetResp();
        BeanUtils.copyProperties(resp, orderVO);

        List<OrderActionRecordVO> recordList = orderActionRecordDAO.list(req.getOrderNumber());

        for (OrderActionRecordVO record : recordList) {
            AdminPainterOrderActionRecordResp r = new AdminPainterOrderActionRecordResp();
            BeanUtils.copyProperties(r, record);
            resp.getActionRecordList().add(r);
        }
        return resp;
    }


    @Action(name = "admin.painter.record.add", desc = "")
    public CommonResultResp addRecord(CommandContext ctxt, AdminPainterRecordAddReq req) {

        // save the img
        String fileName = thumbService.uploadThumb(req.getImg());

        orderActionRecordDAO.add(req.getAccountId(), req.getOrderNumber(),
                fileName, req.getRemark(), req.getActionType());

        return CommonResultResp.success();
    }


}
