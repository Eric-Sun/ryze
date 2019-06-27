package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class PostCursorDetailResp {
    @Parameter(desc = "")
    private int cursor;
    @Parameter(desc = "")
    private int pageNum;

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
