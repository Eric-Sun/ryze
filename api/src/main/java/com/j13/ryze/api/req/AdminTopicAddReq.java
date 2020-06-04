package com.j13.ryze.api.req;


import com.j13.poppy.anno.Parameter;

public class AdminTopicAddReq {
    @Parameter(desc = "")
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
