package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class ItemGetResp {
    @Parameter(desc="item's name")
    private String name;
    @Parameter(desc="item's id")
    private int id;
    @Parameter(desc="item's price")
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
