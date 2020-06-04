package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminTopicDeleteReq {
    @Parameter(desc = "")
    private int topicId;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
