package com.j13.garen.api.req.roomChat;

import com.j13.poppy.anno.Parameter;

public class RoomChatContentResp {
    @Parameter(desc = "")
    private int fromUserId;
    @Parameter(desc = "")
    private String fromUserName;
    @Parameter(desc = "")
    private String fromUserAvatar;
    @Parameter(desc = "")
    private long time;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
