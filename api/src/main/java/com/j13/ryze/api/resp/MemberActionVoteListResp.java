package com.j13.ryze.api.resp;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class MemberActionVoteListResp {

    @Parameter(desc = "")
    private List<MemberActionVoteDetailResp> list = Lists.newLinkedList();

    public List<MemberActionVoteDetailResp> getList() {
        return list;
    }

    public void setList(List<MemberActionVoteDetailResp> list) {
        this.list = list;
    }
}
