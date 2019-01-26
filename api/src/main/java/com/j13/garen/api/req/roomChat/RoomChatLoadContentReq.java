package com.j13.garen.api.req.roomChat;

import com.j13.poppy.anno.Parameter;

public class RoomChatLoadContentReq {

    @Parameter(desc = "")
    private int crId;
    @Parameter(desc = "查找类型，1：往上查找，2：往下查找")
    private int type;
    @Parameter(desc = "查询的相关位置")
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }
}
