package com.j13.ryze.vos.course;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 对应的数据库course的表
 */
public class CourseInfo {
    private int id;
    private String name;
    private int type;
    private String data;
    private int status;
    // 第一备注：课程名下面的那行字
    private String tips1;
    // 第二备注：红色小气泡里的内容
    private String tips2;
    // 标准价格
    private float price;
    // 页面显示的打折价格
    private float discountedPrice;

    // data中的数据JSON解析出来的内容，整个课程的步骤
    private List<CourseStep> stepList = Lists.newLinkedList();



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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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

    public List<CourseStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<CourseStep> stepList) {
        this.stepList = stepList;
    }
}
