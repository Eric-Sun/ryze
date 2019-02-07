package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class TopicDeleteReq {
    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int topicId;

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
