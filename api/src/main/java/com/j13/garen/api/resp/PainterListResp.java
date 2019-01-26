package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

import java.util.List;

public class PainterListResp {

    @Parameter(desc="列表")
    private List<PainterSimpleResp> list;

    public List<PainterSimpleResp> getList() {
        return list;
    }

    public void setList(List<PainterSimpleResp> list) {
        this.list = list;
    }
}
