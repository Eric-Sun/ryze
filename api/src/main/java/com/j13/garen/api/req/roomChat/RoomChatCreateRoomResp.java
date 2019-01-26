package com.j13.garen.api.req.roomChat;

import com.j13.poppy.anno.Parameter;

public class RoomChatCreateRoomResp {
    @Parameter(desc="")
    private int crId;

    public int getCrId() {
        return crId;
    }

    public void setCrId(int crId) {
        this.crId = crId;
    }
}
