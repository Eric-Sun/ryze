package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.JedisManager;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.resp.AdminReplyDetailResp;
import com.j13.ryze.api.resp.Level2ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyDetailResp;
import com.j13.ryze.api.resp.ReplyListResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.facades.ReplyFacade;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ReplyService {

    private static Logger LOG = LoggerFactory.getLogger(ReplyService.class);
    private static String SIMPLE_REPLY_CATALOG = "simplReply";
    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    CommonJedisManager commonJedisManager;
    @Autowired
    ImgService imgService;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;


    /**
     * 如果返回null，这个reply已经被删除
     *
     * @param replyId
     * @return
     */
    public ReplyVO getSimpleReply(int replyId) {
        ReplyVO vo = commonJedisManager.get(SIMPLE_REPLY_CATALOG, replyId, ReplyVO.class);
        if (vo == null) {
            LOG.debug("simpleReply not find in cache. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
            vo = replyDAO.get(replyId);
            if (vo == null)
                return null;
            commonJedisManager.set(SIMPLE_REPLY_CATALOG, replyId, vo);
            LOG.debug("simpleReply reset. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        } else {
            LOG.debug("simpleReply exist. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        }
        return vo;
    }

    /**
     * 解析VO中的imgList，赋值到Resp对象中
     *
     * @param r
     * @param vo
     */
    public void parseImgList(AdminReplyDetailResp r, ReplyVO vo) {
        // imgList info
        String imgListStr = vo.getImgListStr();
        List<String> imgIdList = JSON.parseArray(imgListStr, String.class);

        for (String imgIdStr : imgIdList) {
            ImgVO imgVO = new ImgVO();
            int imgId = new Integer(imgIdStr);
            String url = imgService.getFileUrl(imgId);
            imgVO.setUrl(url);
            imgVO.setId(imgId);
            vo.getImgList().add(imgVO);
        }
    }


    /**
     * 处理reply.list和reply.reverseList接口中的共用方法
     *
     * @param post
     * @param list
     * @param resp
     */
    public void handleReplyList(PostVO post, List<ReplyVO> list, ReplyListResp resp) {
        for (ReplyVO vo : list) {
            // 获得一级评论的数据
            ReplyDetailResp r = new ReplyDetailResp();
            BeanUtils.copyProperties(r, vo);
            userService.setUserInfoForReply(post.getAnonymous(), r, vo.getUserId());
            resp.getData().add(r);

            SizeObject replySize = new SizeObject();
            replySize.setSize(0);
            // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
            // 组成集合之后通过updatetime排序之后按照这个参数取值
            int level2DefaultSize = 2;
            List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

            findAllChildReply(post.getAnonymous(), vo, level2DefaultSize, replySize, tmpLevel2ReplyList);


            Collections.sort(tmpLevel2ReplyList);
            List<ReplyVO> tmpFinalList = null;
            if (tmpLevel2ReplyList.size() >= 2) {
                tmpFinalList = tmpLevel2ReplyList.subList(0, level2DefaultSize);
            } else {
                tmpFinalList = tmpLevel2ReplyList;
            }


            for (ReplyVO finalVO : tmpFinalList) {
                Level2ReplyDetailResp level2Resp = new Level2ReplyDetailResp();
                BeanUtils.copyProperties(level2Resp, finalVO);
                userService.setUserInfoForReply(post.getAnonymous(), level2Resp, level2Resp.getUserId());
                r.getReplyList().add(level2Resp);
            }

            r.setReplySize(replySize.getSize());
        }
    }

    private void findAllChildReply(int postAnonymous, ReplyVO replyVO, int level2DefaultSize,
                                   SizeObject replySize, List<ReplyVO> tmpLevel2ReplyList) {
        List<ReplyVO> list = replyDAO.lastReplylist(replyVO.getReplyId(), 0, level2DefaultSize);
        int listSize = replyDAO.lastReplylistSize(replyVO.getReplyId());
        replySize.setSize(replySize.getSize() + listSize);
        for (ReplyVO vo : list) {
            tmpLevel2ReplyList.add(vo);
            UserVO replyUser = userService.getUserInfo(replyVO.getUserId());
            vo.setLastReplyUserName(replyUser.getNickName());
            vo.setLastReplyUserId(vo.getUserId());
            if (vo.getAnonymous() == Constants.REPLY_ANONYMOUS.ANONYMOUS ||
                    postAnonymous == Constants.REPLY_ANONYMOUS.ANONYMOUS) {
                vo.setLastReplyUserName(replyUser.getAnonNickName());
            }

            findAllChildReply(postAnonymous, vo, level2DefaultSize, replySize, tmpLevel2ReplyList);

        }
    }

    public ReplyDetailResp handleReplyDetail(int replyId) {

        ReplyVO vo = replyDAO.get(replyId);
        PostVO post = postService.getSimplePost(vo.getPostId());
        // 获得一级评论的数据
        ReplyDetailResp r = new ReplyDetailResp();
        BeanUtils.copyProperties(r, vo);
        userService.setUserInfoForReply(post.getAnonymous(), r, vo.getUserId());

        ReplyService.SizeObject replySize = new SizeObject();
        replySize.setSize(0);
        // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
        // 组成集合之后通过updatetime排序之后按照这个参数取值
        int level2DefaultSize = 5;
        List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

        findAllChildReply(post.getAnonymous(), vo, level2DefaultSize, replySize, tmpLevel2ReplyList);

        Collections.sort(tmpLevel2ReplyList);

        for (ReplyVO finalVO : tmpLevel2ReplyList) {
            Level2ReplyDetailResp level2Resp = new Level2ReplyDetailResp();
            BeanUtils.copyProperties(level2Resp, finalVO);
            userService.setUserInfoForReply(post.getAnonymous(), level2Resp, level2Resp.getUserId());

            r.getReplyList().add(level2Resp);
        }
        r.setReplySize(replySize.getSize());
        return r;
    }

    /**
     * size对象
     */
    class SizeObject {
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }


}
