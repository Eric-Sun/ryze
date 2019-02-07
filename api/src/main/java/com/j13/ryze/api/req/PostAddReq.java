package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostAddReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int topicId;
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

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
