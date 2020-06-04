package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class AdminTopicUpdateReq {
    @Parameter(desc = "")
    private int topicId;
    @Parameter(desc = "")
    private String topicName;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
