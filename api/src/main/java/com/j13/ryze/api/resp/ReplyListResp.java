package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class ReplyListResp {
    @Parameter(desc = "")
    private List<ReplyDetailResp> data = Lists.newLinkedList();
    @Parameter(desc = "当前已经记录的cursor")
    private PostCursorDetailResp cursorInfo;

    public PostCursorDetailResp getCursorInfo() {
        return cursorInfo;
    }

    public void setCursorInfo(PostCursorDetailResp cursorInfo) {
        this.cursorInfo = cursorInfo;
    }

    public List<ReplyDetailResp> getData() {
        return data;
    }

    public void setData(List<ReplyDetailResp> data) {
        this.data = data;
    }
}
