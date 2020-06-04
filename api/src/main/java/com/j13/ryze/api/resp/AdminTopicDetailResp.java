package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminTopicDetailResp {
    @Parameter(desc = "")
    private int topicId;
    @Parameter(desc = "")
    private String topicName;
    @Parameter(desc = "")
    private long createtime;

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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }
}
