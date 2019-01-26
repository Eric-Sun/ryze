package com.j13.garen.api.req.roomChat;

import com.j13.poppy.anno.Parameter;

public class RoomChatInitRoomReq {

    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int crId;

    public int getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
