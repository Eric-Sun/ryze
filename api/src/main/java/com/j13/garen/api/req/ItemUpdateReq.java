package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ItemUpdateReq {

    @Parameter(desc="item id")
    private int itemId;
    @Parameter(desc="item's name")
    private String name;
    @Parameter(desc="item's price")
    private float price;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

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
