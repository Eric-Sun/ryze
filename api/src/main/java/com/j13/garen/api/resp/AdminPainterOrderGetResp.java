package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPainterOrderGetResp {
    @Parameter(desc = "item's id")
    private int itemId;
    @Parameter(desc = "item's name")
    private String itemName;
    @Parameter(desc = "create time. formatted to timestamp")
    private long createtime;
    @Parameter(desc = "order status. see the fucking docs.")
    private int status;
    @Parameter(desc = "order uploaded img.")
    private String img;
    @Parameter(desc="")
    private String remark;
    @Parameter(desc="")
    private String orderNumber;
    @Parameter(desc="")
    private List<AdminPainterOrderActionRecordResp> actionRecordList = Lists.newLinkedList();

    public List<AdminPainterOrderActionRecordResp> getActionRecordList() {
        return actionRecordList;
    }

    public void setActionRecordList(List<AdminPainterOrderActionRecordResp> actionRecordList) {
        this.actionRecordList = actionRecordList;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
