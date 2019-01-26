package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;
import org.apache.commons.fileupload.FileItem;

public class AdminPainterRecordAddReq {
    @Parameter(desc = "")
    private int accountId;
    @Parameter(desc = "")
    private int actionType;
    @Parameter(desc = "")
    private String orderNumber;
    @Parameter(desc = "")
    private FileItem img;
    @Parameter(desc = "")
    private String remark;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public FileItem getImg() {
        return img;
    }

    public void setImg(FileItem img) {
        this.img = img;
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
}
