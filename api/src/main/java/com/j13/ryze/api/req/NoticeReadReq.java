package com.j13.ryze.api.req;

import com.j13.poppy.anno.Parameter;

public class NoticeReadReq {
    @Parameter(desc = "")
    private int noticeId;

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }
}
