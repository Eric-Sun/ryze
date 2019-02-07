package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AddTopicReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String content;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
