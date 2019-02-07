package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AddTopicResp {
    @Parameter(desc = "")
    private int topicId;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
