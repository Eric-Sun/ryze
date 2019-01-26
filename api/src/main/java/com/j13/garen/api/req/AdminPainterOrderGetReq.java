package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminPainterOrderGetReq {

    @Parameter(desc="订单id")
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
