package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.JedisManager;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.facades.ReplyFacade;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.*;
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
    private static String REPLY_LIST_CATALOG = "replyList";
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
    @Autowired
    NoticeService noticeService;
    @Autowired
    IAcsClientService iAcsClientService;
    @Autowired
    PostDAO postDAO;
    @Autowired
    WechatAPIService wechatAPIService;


    /**
     * 如果返回null，这个reply已经被删除
     *
     * @param replyId
     * @return
     */
    public ReplyVO getSimpleReply(int replyId) {
        ReplyVO vo = commonJedisManager.get(SIMPLE_REPLY_CATALOG, replyId, ReplyVO.class);
        if (vo == null) {
            LOG.info("simpleReply not find in cache. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
            vo = replyDAO.get(replyId);
            if (vo == null)
                return null;
            commonJedisManager.set(SIMPLE_REPLY_CATALOG, replyId, vo);
            LOG.info("simpleReply reset. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        } else {
            LOG.info("simpleReply exist. catalog={},replyId={}", SIMPLE_REPLY_CATALOG, replyId);
        }
        return vo;
    }

    /**
     * 解析VO中的imgList，赋值到Resp对象中
     *
     * @param vo
     */
    public void parseImgList(ReplyVO vo) {
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
     * @param list
     */
    public void handleReplyList(List<ReplyVO> list) {
        for (ReplyVO vo : list) {
            // 获得一级评论的数据
            parseImgList(vo);
            userService.setUserInfoForReply(vo, vo.getUserId());

            SizeObject replySize = new SizeObject();
            replySize.setSize(0);
            // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
            // 组成集合之后通过updatetime排序之后按照这个参数取值
            int level2DefaultSize = 2;
            List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

            findAllChildReply(vo, level2DefaultSize, replySize, tmpLevel2ReplyList);


            Collections.sort(tmpLevel2ReplyList);
            List<ReplyVO> tmpFinalList = null;
            if (tmpLevel2ReplyList.size() >= 2) {
                tmpFinalList = tmpLevel2ReplyList.subList(0, level2DefaultSize);
            } else {
                tmpFinalList = tmpLevel2ReplyList;
            }


            for (ReplyVO finalVO : tmpFinalList) {
                userService.setUserInfoForReply(finalVO, finalVO.getUserId());
                vo.getReplyList().add(finalVO);
            }

            vo.setReplySize(replySize.getSize());
        }
    }

    private void findAllChildReply(ReplyVO replyVO, int level2DefaultSize,
                                   SizeObject replySize, List<ReplyVO> tmpLevel2ReplyList) {
        List<ReplyVO> list = replyDAO.lastReplylist(replyVO.getReplyId(), 0, level2DefaultSize);
        int listSize = replyDAO.lastReplylistSize(replyVO.getReplyId());
        replySize.setSize(replySize.getSize() + listSize);
        for (ReplyVO vo : list) {
            tmpLevel2ReplyList.add(vo);
            UserVO replyUser = userService.getUserInfo(replyVO.getUserId());
            vo.setLastReplyUserName(replyUser.getNickName());
            vo.setLastReplyUserId(vo.getUserId());
            findAllChildReply(vo, level2DefaultSize, replySize, tmpLevel2ReplyList);

        }
    }

    public ReplyVO handleReplyDetail(int replyId, int pageNum, int size) {

        ReplyVO vo = replyDAO.get(replyId);
        PostVO post = postService.getSimplePost(vo.getPostId());
        // 获得一级评论的数据
        ReplyDetailResp r = new ReplyDetailResp();
        userService.setUserInfoForReply(vo, vo.getUserId());
        BeanUtils.copyProperties(r, vo);

        ReplyService.SizeObject replySize = new SizeObject();
        replySize.setSize(0);
        // 二级评论默认显示2个，其余显示一个总数，搜索的时候用这个参数作为size，
        // 组成集合之后通过updatetime排序之后按照这个参数取值
        int level2DefaultSize = 100;
        List<ReplyVO> tmpLevel2ReplyList = Lists.newLinkedList();

        findAllChildReply(vo, level2DefaultSize, replySize, tmpLevel2ReplyList);

        Collections.sort(tmpLevel2ReplyList);

        for (ReplyVO finalVO : tmpLevel2ReplyList) {
            parseImgList(finalVO);
            userService.setUserInfoForReply(finalVO, finalVO.getUserId());
            vo.getReplyList().add(finalVO);
        }
        vo.setReplySize(replySize.getSize());
        return vo;
    }

    /**
     * 插入reply
     *
     * @param userId
     * @param barId
     * @param postId
     * @param content
     * @param anonymous
     * @param lastReplyId
     * @param imgListStr
     * @return
     */
    public int add(int userId, int barId, int postId, String content, int anonymous, int lastReplyId, String imgListStr, boolean isScan) {
        if (isScan) {
//            boolean b = iAcsClientService.scan(content);
            boolean b = wechatAPIService.msgCheck(content);
            if (b == false) {
                throw new CommonException(ErrorCode.Common.CONTENT_ILLEGAL);
            }
        }
        int id = replyDAO.add(userId, barId, postId,
                content, anonymous, lastReplyId, imgListStr);
        postDAO.addReplyCount(postId);
        postDAO.updateTime(postId);
        return id;
    }

    /**
     * 用户获取某一个帖子的reply列表
     *
     * @param postId
     * @param pageNum
     * @param size
     * @return
     */
    public List<ReplyVO> list(int postId, int pageNum, int size) {
        // 尝试从缓存中获取该分页的内容，如果不存在的话
        return getReplyListFromCache(postId, pageNum);
    }


    /**
     * @param postId
     * @param pageNum
     * @return
     */
    public List<ReplyVO> getReplyListFromCache(int postId, int pageNum) {
        List<ReplyVO> replyVOList = commonJedisManager.getArray(SIMPLE_REPLY_CATALOG, postId + ":" + pageNum, ReplyVO.class);
        if (replyVOList == null) {
            LOG.info("replyList cache not find in cache. catalog={},postId={},pageNum={}", SIMPLE_REPLY_CATALOG, postId, pageNum);
            // 重新组建这个页
            replyVOList = rebuildReplyList(postId, pageNum);
            commonJedisManager.set(SIMPLE_REPLY_CATALOG, postId + ":" + pageNum, replyVOList);
            LOG.info("replyList cache reset. catalog={},postId={},pageNum={}", SIMPLE_REPLY_CATALOG, postId, pageNum);
        } else {
            LOG.info("replyList cache exist. catalog={},postId={},pageNum={}", SIMPLE_REPLY_CATALOG, postId, pageNum);
        }
        return replyVOList;
    }

    /**
     * 在添加回复的时候调用，刷新cache，刷新的是最后一个页的其他的不变
     *
     * @param postId
     */
    public void updateReplyListCache(int postId) {

        // 尝试获取一级评论的size
        int level1Size = getLevel1ReplySize(postId);

        int updatePageNum = 0;

        if (level1Size % Constants.Reply.REPLY_SIZE_PER_PAGE == 0) {
            updatePageNum = level1Size / Constants.Reply.REPLY_SIZE_PER_PAGE - 1;
        } else {
            updatePageNum = level1Size / Constants.Reply.REPLY_SIZE_PER_PAGE;
        }

        List<ReplyVO> replyVOList = rebuildReplyList(postId, updatePageNum);
        commonJedisManager.set(SIMPLE_REPLY_CATALOG, postId + ":" + updatePageNum, replyVOList);
        LOG.info("replyList cache updated. catalog={},postId={},pageNum={}", SIMPLE_REPLY_CATALOG, postId, updatePageNum);
    }

    /**
     * 获取post的一级评论的总量
     *
     * @param postId
     * @return
     */
    public int getLevel1ReplySize(int postId) {
        return replyDAO.level1ReplyCount(postId);
    }


    /**
     * 重新组建基于PostId和pageNum的replyList列表
     * 在cache失效、新增二三级回复和删除回复的时候调用
     *
     * @param postId
     * @param pageNum
     * @return
     */
    private List<ReplyVO> rebuildReplyList(int postId, int pageNum) {
        List<ReplyVO> list = replyDAO.list(postId, pageNum, Constants.Reply.REPLY_SIZE_PER_PAGE);
        handleReplyList(list);
        return list;
    }

    /**
     * 获取某一个帖子下所有回复的数量，用于admin后台展示
     * @param postId
     * @return
     */
    public int getReplyCount(int postId) {
        return replyDAO.getReplyCount(postId);
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
