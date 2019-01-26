package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class OrderListReq {
    @Parameter(desc = "order status. when query all status. set -1")
    private int status;
    @Parameter(desc = "size per page")
    private int sizePerPage;
    @Parameter(desc = "page number. beginning at 0")
    private int pageNum;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getSizePerPage() {
        return sizePerPage;
    }

    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
