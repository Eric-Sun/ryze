package com.j13.ryze.services;

import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.PostCursorDAO;
import com.j13.ryze.vos.PostCursorVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostCursorService {

    @Autowired
    PostCursorDAO postCursorDAO;

    /**
     * 获取cursor，如果没有的话返回都是0的cursor对象
     *
     * @param userToken
     * @param postId
     */
    public PostCursorVO getCursor(String userToken, int postId) {
        // 如果userId==0的话，返回0
        if (userToken.equals("")) {
            PostCursorVO vo = new PostCursorVO();
            vo.setPageNum(0);
            vo.setCursor(0);
            return vo;
        }
        boolean b = postCursorDAO.checkExist(userToken, postId);
        if (b) {
            PostCursorVO vo = postCursorDAO.getCursor(userToken, postId);
            Logger.COMMON.info("get cursor. userToken={},postId={},cursor={},pageNum={}", userToken, postId, vo.getCursor(), vo.getPageNum());
            return vo;
        } else {
            postCursorDAO.add(userToken, postId, 0, 0);
            Logger.COMMON.info("add cursor. userToken={},postId={}", userToken, postId);
            PostCursorVO vo = new PostCursorVO();
            vo.setPageNum(0);
            vo.setCursor(0);
            return vo;
        }
    }

    public void updateCursor(String userToken, int postId, int cursor, int pageNum) {
        // 保护机制，同getCursor
        if (userToken.equals("")) {
            return;
        }
        postCursorDAO.updateCursor(userToken, postId, cursor, pageNum);
        Logger.COMMON.info("update cursor. userToken={},postId={},cursor={},pageNum={}", userToken, postId, cursor, pageNum);
    }

}
