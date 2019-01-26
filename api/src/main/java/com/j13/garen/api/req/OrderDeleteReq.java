package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class OrderDeleteReq {
    @Parameter(desc="order number")
    private String orderNumber;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
