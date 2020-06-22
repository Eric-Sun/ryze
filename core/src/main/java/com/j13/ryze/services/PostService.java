package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.j13.poppy.config.PropertiesConfiguration;
import com.j13.poppy.exceptions.CommonException;
import com.j13.ryze.api.req.PostDetailResp;
import com.j13.ryze.cache.PostIdListCache;
import com.j13.ryze.cache.TopicCache;
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
import java.util.Collections;
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
    @Autowired
    PostIdListCache postIdListCache;
    @Autowired
    PostTopicDAO postTopicDAO;
    @Autowired
    TopicCache topicCache;


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

        // user info
        UserVO user = userService.getUserInfo(vo.getUserId());
        vo.setUserName(user.getNickName());
        vo.setUserAvatarUrl(user.getAvatarUrl());

        // 查询帖子的话题信息

        List<Integer> topicIdList = postTopicDAO.getAllTopicIds(postId);
        for (Integer topicId : topicIdList) {
            TopicVO topicVO = topicCache.get(topicId);
            vo.getTopicList().add(topicVO);
        }


        return vo;
    }

    public void flushSimplePost(int postId) {
        PostVO vo = postDAO.get(postId);
        commonJedisManager.set(SIMPLE_POST_CATALOG, postId, vo);
        LOG.debug("simplePost flushed. catalog={},postId={}", SIMPLE_POST_CATALOG, postId);
    }


    public void offline(int postId) {
        postDAO.offline(postId);
        postIdListCache.removePostId(postId);
        LOG.info("post offline done. postId={}", postId);
    }


    public void update(int postId, String content, String title, int anonymous, int type, String imgListStr) {
        postDAO.update(postId, content, title, anonymous, type, imgListStr);
        // update cache
        PostVO vo = postDAO.get(postId);
        commonJedisManager.set(SIMPLE_POST_CATALOG, postId, vo);
        LOG.info("update the post object cache. postId={}", postId);
    }

    public int add(int uid, int barId, String title, String content, int anonymous, int type, String imgList, List<Integer> topicIdList) {
        int postId = postDAO.add(uid, barId, title, content, anonymous, type, imgList);
        barDAO.addPostCount(barId);
        // 添加帖子的topic列表
        for (Integer topicId : topicIdList) {
            postTopicDAO.insert(postId, topicId);
        }
        return postId;
    }

    public int addOffLine(int uid, int barId, String title, String content, int anonymous, int type, String imgList) {
        int postId = postDAO.addOffline(uid, barId, title, content, anonymous, type, imgList);
        return postId;
    }

    /**
     * 客户端模块用到
     *
     * @param barId
     * @param type
     * @param pageNum
     * @param size
     * @return
     */
    public List<PostVO> list(int barId, int type, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
//        List<Integer> postIdList = null;
//        if (type == Constants.POST_TYPE.ALL_TYPE)
//            postIdList = postDAO.list(barId, pageNum, size);
//        else
//            postIdList = postDAO.listByType(barId, type, pageNum, size);
        List<String> postIdLis = postIdListCache.randomNPostId(size);

        for (String postIdStr : postIdLis) {
            Integer postId = new Integer(postIdStr);
            PostVO vo = getSimplePost(postId);

            list.add(vo);
        }
        return list;
    }

    /**
     * admin模块用到
     *
     * @param barId
     * @param type
     * @param pageNum
     * @param size
     * @return
     */
    public List<PostVO> listForAdmin(int barId, int type, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
        List<Integer> postIdList = null;
        if (type == Constants.POST_TYPE.ALL_TYPE)
            postIdList = postDAO.list(barId, pageNum, size);
        else
            postIdList = postDAO.listByType(barId, type, pageNum, size);

        for (Integer postIdStr : postIdList) {
            Integer postId = new Integer(postIdStr);
            PostVO vo = getSimplePost(postId);


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

            list.add(vo);
        }
        return list;
    }

    public List<PostVO> deletedList(int barId, int pageNum, int size) {
        List<PostVO> list = Lists.newLinkedList();
        List<Integer> postIdList = postDAO.deletedList(barId, pageNum, size);
        for (Integer postId : postIdList) {
            PostVO vo = getSimplePost(postId);

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
     * 获得该用户之前已经看过的精华帖子列表
     *
     * @param userToken
     * @return
     */
    public List<Integer> showedStarPostIdList(String userToken) {
        return starPostShowlogDAO.list(userToken);
    }

    public List<Integer> starPostIdList() {
        return starPostDAO.listPostId();
    }


    /**
     * 添加加精内容的日志，表示这个用户已经曝光过这个加精日志了
     *
     * @param userToken
     * @param postId
     */
    public void addStarPostShowlog(String userToken, Integer postId) {
        starPostShowlogDAO.add(userToken, postId);
    }

    /**
     * 更新所有的topicId，该删除删除，该增加的增加
     *
     * @param postId
     * @param topicIdList
     */
    public void updateTopicList(int postId, List<Integer> topicIdList) {
        List<Integer> oldTopicIdList = postTopicDAO.getAllTopicIds(postId);
        List<Integer> tmpOldTopicIdList = Lists.newLinkedList();
        List<Integer> tmpnewTopicIdList = Lists.newLinkedList();
        Collections.copy(tmpOldTopicIdList, oldTopicIdList);
        Collections.copy(tmpnewTopicIdList, topicIdList);

        // 要删除的id列表
        tmpOldTopicIdList.removeAll(topicIdList);
        // 要增加的列表
        tmpnewTopicIdList.remove(oldTopicIdList);

        for (Integer needDeleteId : tmpOldTopicIdList) {
            postTopicDAO.deletePostTopic(needDeleteId);
        }
        for (Integer needAddId : tmpnewTopicIdList) {
            postTopicDAO.insert(postId, needAddId);
        }
    }
}
