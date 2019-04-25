package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class MemberActionPushVoteResp {
    @Parameter(desc = "")
    private int voteId;

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }
}
