package com.j13.garen.api.req.roomChat;

import com.j13.poppy.anno.Parameter;

public class RoomChatSendContentReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int crId;
    @Parameter(desc = "")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
