package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ItemAddReq {
    @Parameter(desc="商品名称")
    private String name;
    @Parameter(desc="价格")
    private float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
