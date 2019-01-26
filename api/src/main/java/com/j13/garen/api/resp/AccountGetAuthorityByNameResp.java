package com.j13.garen.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class AccountGetAuthorityByNameResp {

    @Parameter(desc="authority object list")
    private List<AuthorityGetResp> data = Lists.newLinkedList();

    public List<AuthorityGetResp> getData() {
        return data;
    }

    public void setData(List<AuthorityGetResp> data) {
        this.data = data;
    }
}
