package com.j13.ryze.api.req;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AdminPostUpdateTopicListReq {
    @Parameter(desc = "")
    private int postId;
    @Parameter(desc = "")
    private List<Integer> topicIdList = Lists.newLinkedList();

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<Integer> getTopicIdList() {
        return topicIdList;
    }

    public void setTopicIdList(List<Integer> topicIdList) {
        this.topicIdList = topicIdList;
    }
}
