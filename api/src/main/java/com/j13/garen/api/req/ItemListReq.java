package com.j13.garen.api.req;

import com.j13.poppy.anno.Parameter;

public class ItemListReq {
    @Parameter(desc="每页条数")
    private int sizePerPage;
    @Parameter(desc="页数")
    private int pageNum;

    public int getSizePerPage() {
        return sizePerPage;
    }

    public void setSizePerPage(int sizePerPage) {
        this.sizePerPage = sizePerPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
