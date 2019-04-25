package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.anno.NeedToken;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.MemberActionAgreeVoteReq;
import com.j13.ryze.api.req.MemberActionDisagreeVoteReq;
import com.j13.ryze.api.req.MemberActionPushVoteReq;
import com.j13.ryze.api.req.MemberActionVoteListReq;
import com.j13.ryze.api.resp.MemberActionPushVoteResp;
import com.j13.ryze.api.resp.MemberActionVoteDetailResp;
import com.j13.ryze.api.resp.MemberActionVoteListResp;
import com.j13.ryze.api.resp.MemberActionVotePostOfflineEvidenceResp;
import com.j13.ryze.destiny.DestinyConstants;
import com.j13.ryze.destiny.PostOfflineVoteEvidence;
import com.j13.ryze.destiny.VoteService;
import com.j13.ryze.services.UserService;
import com.j13.ryze.vos.VoteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberActionFacade {


    @Autowired
    VoteService voteService;

    @Action(name = "memberAction.voteList")
    @NeedToken
    public MemberActionVoteListResp voteList(CommandContext ctxt, MemberActionVoteListReq req) {
        MemberActionVoteListResp resp = new MemberActionVoteListResp();
        List<VoteVO> voteVOList = voteService.list(req.getPageNum(), req.getSize());
        for (VoteVO vo : voteVOList) {
            MemberActionVoteDetailResp detailResp = new MemberActionVoteDetailResp();
            BeanUtils.copyProperties(detailResp, vo);
            if (vo.getType() == DestinyConstants.Vote.Type.POST_OFFLINE_VOTE) {
                PostOfflineVoteEvidence evidence = (PostOfflineVoteEvidence) vo.getEvidenceObject();
                MemberActionVotePostOfflineEvidenceResp evidenceResp = new MemberActionVotePostOfflineEvidenceResp();
                BeanUtils.copyProperties(evidenceResp, evidence);
                detailResp.setEvidenceObject(evidenceResp);
                resp.getList().add(detailResp);
            }
        }
        return resp;
    }


    @Action(name = "memberAction.pushVote")
    @NeedToken
    public MemberActionPushVoteResp pushVote(CommandContext ctxt, MemberActionPushVoteReq req) {
        MemberActionPushVoteResp resp = new MemberActionPushVoteResp();
        // check is member 40001
        int userId = ctxt.getUid();
        int voteId = voteService.pushVote(userId, req.getResourceId(), req.getType(), req.getReason());
        resp.setVoteId(voteId);
        return resp;
    }


    @Action(name = "memberAction.agreeVote")
    @NeedToken
    public CommonResultResp agreeVote(CommandContext ctxt, MemberActionAgreeVoteReq req) {
        int userId = ctxt.getUid();
        voteService.agreeOneVote(req.getVoteId(), userId);
        return CommonResultResp.success();
    }

    @Action(name = "memberAction.disagreeVote")
    @NeedToken
    public CommonResultResp disagreeVote(CommandContext ctxt, MemberActionDisagreeVoteReq req) {
        int userId = ctxt.getUid();
        voteService.disagreeOneVote(req.getVoteId(), userId);
        return CommonResultResp.success();
    }

}
