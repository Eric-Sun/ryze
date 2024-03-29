package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;

import java.util.List;

public class CourseDetailResp {
    private int id;
    private String name;
    private int type;
    private List<Object> data= Lists.newLinkedList();
    // 第一备注：课程名下面的那行字
    private String tips1;
    // 第二备注：红色小气泡里的内容
    private String tips2;
    // 标准价格
    private float price;
    // 页面显示的打折价格
    private float discountedPrice;

    public String getTips1() {
        return tips1;
    }

    public void setTips1(String tips1) {
        this.tips1 = tips1;
    }

    public String getTips2() {
        return tips2;
    }

    public void setTips2(String tips2) {
        this.tips2 = tips2;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(float discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
