package com.j13.garen.api.resp;

import com.j13.poppy.anno.Parameter;

import java.util.List;

public class ResourceGetResourceListByAuthorityIdResp {
    @Parameter(desc="resource id list")
    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
