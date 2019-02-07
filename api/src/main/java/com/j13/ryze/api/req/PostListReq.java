package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class PostListReq {
    @Parameter(desc = "")
    private int barId;
    @Parameter(desc = "")
    private int topicId;
    @Parameter(desc = "")
    private int pageNum;
    @Parameter(desc = "")
    private int size;

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
}
