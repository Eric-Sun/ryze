package com.j13.ryze.services;

import com.google.common.collect.Lists;
import com.j13.ryze.api.resp.AdminLevelInfoResp;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.vos.BarVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminLevelInfoService {


    @Autowired
    BarDAO barDAO;
    @Autowired
    PostDAO postDAO;
    @Autowired
    ReplyDAO replyDAO;


    /**
     * 0:bar,1:post,2:reply
     *
     * @param id
     * @param type
     * @return
     */
    public List<AdminLevelInfoResp> findLevelInfo(int id, int type) {
        List<AdminLevelInfoResp> list = Lists.newLinkedList();

        if (type == 1) {
            // post
            PostVO post = postDAO.get(id);
            String barName = barDAO.getBarName(post.getBarId());
            AdminLevelInfoResp r = new AdminLevelInfoResp();
            r.setBrief(barName);
            r.setId(post.getBarId());
            r.setType(0);
            list.add(r);
            AdminLevelInfoResp r1 = new AdminLevelInfoResp();
            r1.setBrief(post.getTitle());
            r1.setId(post.getPostId());
            r1.setType(1);
            list.add(r1);
        } else {
            // reply
            ReplyVO reply0 = replyDAO.get(id);
            PostVO post = postDAO.get(reply0.getPostId());
            String barName = barDAO.getBarName(reply0.getBarId());

            AdminLevelInfoResp barResp = new AdminLevelInfoResp();
            barResp.setBrief(barName);
            barResp.setId(post.getBarId());
            barResp.setType(0);
            list.add(barResp);

            AdminLevelInfoResp postResp = new AdminLevelInfoResp();
            postResp.setBrief(post.getTitle());
            postResp.setId(post.getPostId());
            postResp.setType(1);
            list.add(postResp);

            ReplyLevelCount count = new ReplyLevelCount();

            List<AdminLevelInfoResp> tmpList = Lists.newLinkedList();
            findReply(tmpList, reply0.getReplyId(), count);

            int tmpCount = tmpList.size() - 1;
            for (AdminLevelInfoResp r : tmpList) {
                r.setLevel((r.getLevel() - tmpCount) * -1);
            }


            Collections.reverse(tmpList);
            list.addAll(tmpList);

        }
        return list;
    }

    private void findReply(List<AdminLevelInfoResp> list, int replyId, ReplyLevelCount count) {
        ReplyVO reply = replyDAO.get(replyId);
        if (reply.getLastReplyId() == 0) {
            //
            AdminLevelInfoResp replyResp = new AdminLevelInfoResp();
            replyResp.setBrief(reply.getContent());
            replyResp.setLevel(count.getCount());
            replyResp.setType(2);
            replyResp.setId(reply.getReplyId());
            list.add(replyResp);
            return;
        } else {
            // 还有reply可以获取
            AdminLevelInfoResp replyResp = new AdminLevelInfoResp();
            replyResp.setBrief(reply.getContent());
            replyResp.setLevel(count.getCount());
            replyResp.setType(2);
            replyResp.setId(reply.getReplyId());
            list.add(replyResp);

            count.setCount(count.getCount() + 1);
            findReply(list, reply.getLastReplyId(), count);
        }
    }

}

class ReplyLevelCount {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
