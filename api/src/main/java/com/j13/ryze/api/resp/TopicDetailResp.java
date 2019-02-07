package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class TopicDetailResp {

    @Parameter(desc = "")
    private int userId;
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private String content;
    @Parameter(desc = "")
    private long createtime;

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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
