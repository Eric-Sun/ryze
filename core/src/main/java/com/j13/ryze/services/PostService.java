package com.j13.ryze.services;

import com.alibaba.fastjson.JSON;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.resp.AdminPostDetailResp;
import com.j13.ryze.core.Constants;
import com.j13.ryze.daos.BarDAO;
import com.j13.ryze.daos.PostDAO;
import com.j13.ryze.daos.ReplyDAO;
import com.j13.ryze.utils.CommonJedisManager;
import com.j13.ryze.vos.ImgVO;
import com.j13.ryze.vos.PostVO;
import com.j13.ryze.vos.ReplyVO;
import com.j13.ryze.vos.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<PostVO> list = null;
        if (type == Constants.POST_TYPE.ALL_TYPE)
            list = postDAO.list(barId, pageNum, size);
        else
            list = postDAO.listByType(barId, type, pageNum, size);
        for (PostVO vo : list) {
            // user info
            UserVO user = userService.getUserInfo(vo.getUserId());
            vo.setUserName(user.getNickName());
            vo.setUserAvatarUrl(user.getAvatarUrl());

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
        return list;
    }
}
