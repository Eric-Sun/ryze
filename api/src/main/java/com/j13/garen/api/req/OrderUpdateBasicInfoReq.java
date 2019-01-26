package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;
import org.apache.commons.fileupload.FileItem;

public class OrderUpdateBasicInfoReq {
    @Parameter(desc = " order id")
    private int orderId;
    @Parameter(desc = "final price")
    private float finalPrice;
    @Parameter(desc=" item id")
    private int itemId;
    @Parameter(desc="")
    private FileItem img;
    @Parameter(desc="contactMobile")
    private String contactMobile;

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public FileItem getImg() {
        return img;
    }

    public void setImg(FileItem img) {
        this.img = img;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
