package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.CommonException;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.*;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class PostService {
    private static Logger LOG = LoggerFactory.getLogger(PostService.class);
    private static String SIMPLE_POST_CATALOG = "simplePost";

    @Autowired
    ReplyDAO replyDAO;
    @Autowired
    CommonJedisManager commonJedisManager;
    @Autowired
    PostDAO postDAO;
    @Autowired
    UserService userService;
    @Autowired
    ImgService imgService;
    @Autowired
    BarDAO barDAO;
    @Autowired
    CollectionDAO collectionDAO;
    @Autowired
    PropertiesConfiguration configuration;
    @Autowired
    StarPostDAO starPostDAO;
    @Autowired
    StarPostShowlogDAO starPostShowlogDAO;

    private int POST_CONTENT_CUT_LENGTH;

    @PostConstruct
    public void init() {
        POST_CONTENT_CUT_LENGTH = configuration.getIntValue("post.content.cut.length");
    }

    public int replyCount(int postId) {
        int count = replyDAO.replyCount(postId);
        return count;
    }

    public PostVO getSimplePost(int postId) {
        PostVO vo = commonJedisManager.get(SIMPLE_POST_CATALOG, postId, PostVO.class);
        if (vo == null) {
            LOG.debug("simplePost not find in cache. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
            vo = postDAO.get(postId);
            commonJedisManager.set(SIMPLE_POST_CATALOG, postId, vo);
            LOG.debug("simplePost reset. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
        } else {
            LOG.debug("simplePost exist. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
        }


        parsePostImgList(vo);
        return vo;
    }


    public void offline(int postId) {
        postDAO.offline(postId);
        LOG.info("post offline done. postId={}", postId);
    }


    public List<PostVO> list(int barId, int pageNum, int size) {
        return list(barId, Constants.POST_TYPE.ALL_TYPE, pageNum, size);
    }


    public void update(int postId, String content, String title, int anonymous, int type, String imgListStr) {
        postDAO.update(postId, content, title, anonymous, type, imgListStr);
        // update cache
        PostVO vo = postDAO.get(postId);
        commonJedisManager.set(SIMPLE_POST_CATALOG, postId, vo);
        LOG.info("update the post object cache. postId={}", postId);
    }

    public int add(int uid, int barId, String title, String content, int anonymous, int type, String imgList) {
        int postId = postDAO.add(uid, barId, title, content, anonymous, type, imgList);
        barDAO.addPostCount(barId);
        return postId;
    }

    public List<PostVO> list(int barId, int type, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
        List<Integer> postIdList = null;
        if (type == Constants.POST_TYPE.ALL_TYPE)
            postIdList = postDAO.list(barId, pageNum, size);
        else
            postIdList = postDAO.listByType(barId, type, pageNum, size);
        for (Integer postId : postIdList) {
            PostVO vo = getSimplePost(postId);
            // user info
            UserVO user = userService.getUserInfo(vo.getUserId());
            vo.setUserName(user.getNickName());
            vo.setUserAvatarUrl(user.getAvatarUrl());

            list.add(vo);
        }
        return list;
    }

    private void parsePostImgList(PostVO vo) {
        // imgList info
        String imgListStr = vo.getImgListStr();
        List<String> imgIdList = JSON.parseArray(imgListStr, String.class);

        for (String imgIdStr : imgIdList) {
            ImgVO imgVO = new ImgVO();
            int imgId = new Integer(imgIdStr);
            String url = imgService.getFileUrl(imgId);
            imgVO.setUrl(url);
            imgVO.setId(imgId);
            vo.getImgVOList().add(imgVO);
        }
    }


    /**
     * 尝试处理post的内容过长的问题
     */
    public void tryToCutOutContent(PostDetailResp r) {
        if (r.getContent().length() > POST_CONTENT_CUT_LENGTH) {
            r.setContent(r.getContent().substring(0, POST_CONTENT_CUT_LENGTH) + "...");
            r.setIsContentLong(1);
        } else {
            return;
        }

    }

    public List<PostVO> offlineList(int barId, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
        List<Integer> postIdList = postDAO.offlineList(barId, pageNum, size);
        for (Integer postId : postIdList) {
            PostVO vo = getSimplePost(postId);
            // user info
            UserVO user = userService.getUserInfo(vo.getUserId());
            vo.setUserName(user.getNickName());
            vo.setUserAvatarUrl(user.getAvatarUrl());

            list.add(vo);
        }
        return list;
    }

    public List<PostVO> deletedList(int barId, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
        List<Integer> postIdList = postDAO.deletedList(barId, pageNum, size);
        for (Integer postId : postIdList) {
            PostVO vo = getSimplePost(postId);
            // user info
            UserVO user = userService.getUserInfo(vo.getUserId());
            vo.setUserName(user.getNickName());
            vo.setUserAvatarUrl(user.getAvatarUrl());

            list.add(vo);
        }
        return list;
    }

    public int postCount(int barId) {
        return postDAO.postCount(barId);
    }

    public int deletedListCount(int barId) {
        return postDAO.deletedListCount(barId);
    }

    public int offlineListCount(int barId) {
        return postDAO.offlineListCount(barId);
    }

    /**
     * 获得该用户之前已经
     *
     * @param requestUserId
     * @return
     */
    public List<Integer> showedStarPostIdList(int requestUserId) {
        return starPostShowlogDAO.list(requestUserId);
    }

    public List<Integer> starPostIdList() {
        return starPostDAO.listPostId();
    }


    /**
     * 添加加精内容的日志，表示这个用户已经曝光过这个加精日志了
     *
     * @param requestUserId
     * @param postId
     */
    public void addStarPostShowlog(int requestUserId, Integer postId) {
        starPostShowlogDAO.add(postId, requestUserId);
    }
}
