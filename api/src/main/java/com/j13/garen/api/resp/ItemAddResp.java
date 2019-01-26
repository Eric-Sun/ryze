package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

public class ItemAddResp {

    @Parameter(desc="商品id")
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
