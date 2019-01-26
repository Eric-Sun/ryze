package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ItemGetReq {
    @Parameter(desc="item id")
    private int itemId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
