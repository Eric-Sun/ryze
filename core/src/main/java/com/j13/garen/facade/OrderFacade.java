package com.j13.garen.facade;

import com.j13.garen.daos.ImgDAO;
import com.j13.garen.daos.OrderDAO;
import com.j13.garen.api.req.*;
import com.j13.garen.api.resp.*;
import com.j13.garen.services.ImgService;
import com.j13.garen.services.OrderService;
import com.j13.garen.vos.ImgVO;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.garen.core.Constants;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.garen.services.ThumbService;
import com.j13.garen.vos.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderFacade {

    private static Logger LOG = LoggerFactory.getLogger(OrderFacade.class);

    @Autowired
    OrderDAO orderDAO;
    @Autowired
    ThumbService thumbService;
    @Autowired
    OrderService orderService;
    @Autowired
    ImgDAO imgDAO;

    @Autowired
    ImgService imgService;

    @Action(name = "order.add", desc = "add an order by admin")
    public OrderAddResp add(CommandContext ctxt, OrderAddReq req) {
        OrderAddResp resp = new OrderAddResp();
        // save the img
        String orderNumber = orderService.genOrderNumber();


        int orderId = orderDAO.add(req.getUserId(), req.getItemId(),
                req.getFinalPrice(), Constants.OrderStatus.ORDER_CREATED, req.getImgId(), req.getRemark(), orderNumber);
        LOG.info("add order suc. id={}", orderId);
        resp.setOrderId(orderId);
        return resp;
    }

//    @Action(name = "order.updateBasicInfo", desc = " update basic info of an order")
//    public CommonResultResp updateBasicInfo(CommandContext ctxt, OrderUpdateBasicInfoReq req) {
//        CommonResultResp resp = new CommonResultResp();
//        if (req.getImg() == null) {
//            orderDAO.updateBasicInfo(req.getOrderId(), req.getItemId(), req.getFinalPrice(), req.getContactMobile());
//            LOG.info("update order suc. id={}", req.getOrderId());
//        } else {
//            String fileName = thumbService.uploadThumb(req.getImg());
//            orderDAO.updateBasicInfo(req.getOrderId(), req.getItemId(), req.getFinalPrice(), fileName);
//            LOG.info("update order suc. id={},fileName={}", req.getOrderId(), fileName);
//        }
//
//        return resp;
//    }

    @Action(name = "order.updateStatus", desc = " update order's status")
    public CommonResultResp updateStatus(CommandContext ctxt, OrderUpdateStatusReq req) {
        CommonResultResp resp = new CommonResultResp();
        orderDAO.updateStatus(req.getOrderNumber(), req.getStatus());
        LOG.info("update order status suc. orderId={},status={}", req.getOrderNumber(), req.getStatus());
        return resp;
    }

    @Action(name = "order.delete", desc = " delete an order by orderId")
    public CommonResultResp delete(CommandContext ctxt, OrderDeleteReq req) {
        CommonResultResp resp = new CommonResultResp();
        orderDAO.delete(req.getOrderNumber());
        LOG.info("delete order .orderNumber={}", req.getClass());
        return resp;
    }

    @Action(name = "order.get", desc = "get a order by order ID")
    public OrderGetResp get(CommandContext ctxt, OrderGetReq req) {
        OrderGetResp resp = new OrderGetResp();
        OrderVO orderVO = orderDAO.get(req.getOrderNumber());
        ImgVO imgVO = imgService.loadImg(orderVO.getImgId());
        ImgGetResp imgResp = new ImgGetResp();
        BeanUtils.copyProperties(imgResp, imgVO);
        BeanUtils.copyProperties(resp, orderVO);
        resp.setImg(imgResp);
        return resp;
    }

    @Action(name = "order.list", desc = "query order list.")
    public OrderListResp list(CommandContext ctxt, OrderListReq req) {
        OrderListResp resp = new OrderListResp();
        List<OrderVO> list = null;
        if (req.getStatus() == Constants.OrderStatus.QUERY_ALL_STATUS) {
            list = orderDAO.list(req.getSizePerPage(), req.getPageNum());
        } else {
            list = orderDAO.list(req.getSizePerPage(), req.getPageNum(), req.getStatus());
        }
        for (OrderVO vo : list) {
            OrderGetResp r = new OrderGetResp();
            ImgVO imgVO = imgService.loadImg(vo.getImgId());
            ImgGetResp imgResp = new ImgGetResp();
            BeanUtils.copyProperties(imgResp, imgVO);
            BeanUtils.copyProperties(resp, vo);
            r.setImg(imgResp);
            resp.getList().add(r);
        }

        return resp;
    }

    @Action(name = "order.listByUserId", desc = "query order list.")
    public OrderListResp listByUserId(CommandContext ctxt, OrderListByUserIdReq req) {
        OrderListResp resp = new OrderListResp();
        List<OrderVO> list = null;
        list = orderDAO.listByUserId(req.getUserId());
        for (OrderVO vo : list) {
            OrderGetResp r = new OrderGetResp();
            ImgVO imgVO = imgService.loadImg(vo.getImgId());
            ImgGetResp imgResp = new ImgGetResp();
            BeanUtils.copyProperties(imgResp, imgVO);
            BeanUtils.copyProperties(r, vo);
            r.setImg(imgResp);
            r.setStatusStr(orderService.getStatusStr(vo.getStatus()));
            resp.getList().add(r);
        }

        return resp;
    }


    @Action(name = "order.setPainter", desc = "")
    public CommonResultResp setPainter(CommandContext ctxt, OrderSetPainterReq req) {
        orderDAO.setPainter(req.getOrderNumber(), req.getAccountId());
        return CommonResultResp.success();
    }

    @Action(name = "order.uploadImg", desc = "")
    public OrderUploadImgResp uploadImg(CommandContext ctxt, OrderUploadImgReq req) {
        String fileName = thumbService.uploadThumb(req.getImg());

        int id = thumbService.insertOrderImg(fileName);
        OrderUploadImgResp resp = new OrderUploadImgResp();
        resp.setImgId(id);
        return resp;
    }

}
