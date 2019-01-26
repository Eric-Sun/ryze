package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class OrderGetResp {
    @Parameter(desc = "order id")
    private int id;
    @Parameter(desc = "user id")
    private int userId;
    @Parameter(desc = "user's name")
    private String userName;
    @Parameter(desc = "item's id")
    private int itemId;
    @Parameter(desc = "item's name")
    private String itemName;
    @Parameter(desc = "the final price")
    private float finalPrice;
    @Parameter(desc = "create time. formatted to timestamp")
    private long createtime;
    @Parameter(desc = "order status. see the fucking docs.")
    private int status;
    @Parameter(desc = "order uploaded img.")
    private ImgGetResp img;
    @Parameter(desc = "")
    private String remark;
    @Parameter(desc = "")
    private String orderNumber;
    @Parameter(desc = "")
    private int painterId;
    @Parameter(desc = "")
    private String statusStr;


    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public int getPainterId() {
        return painterId;
    }

    public void setPainterId(int painterId) {
        this.painterId = painterId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ImgGetResp getImg() {
        return img;
    }

    public void setImg(ImgGetResp img) {
        this.img = img;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
