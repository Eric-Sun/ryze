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
     * @param userId
     * @param postId
     */
    public PostCursorVO getCursor(int userId, int postId) {
        // 如果userId==0的话，返回0
        if (userId == 0) {
            PostCursorVO vo = new PostCursorVO();
            vo.setPageNum(0);
            vo.setCursor(0);
            return vo;
        }
        boolean b = postCursorDAO.checkExist(userId, postId);
        if (b) {
            PostCursorVO vo = postCursorDAO.getCursor(userId, postId);
            Logger.COMMON.info("get cursor. userId={},postId={},cursor={},pageNum={}", userId, postId, vo.getCursor(), vo.getPageNum());
            return vo;
        } else {
            postCursorDAO.add(userId, postId, 0, 0);
            Logger.COMMON.info("add cursor. userId={},postId={}", userId, postId);
            PostCursorVO vo = new PostCursorVO();
            vo.setPageNum(0);
            vo.setCursor(0);
            return vo;
        }
    }

    public void updateCursor(int userId, int postId, int cursor, int pageNum) {
        // 保护机制，同getCursor
        if (userId == 0) {
            return;
        }
        postCursorDAO.updateCursor(userId, postId, cursor, pageNum);
        Logger.COMMON.info("update cursor. userId={},postId={},cursor={},pageNum={}", userId, postId, cursor, pageNum);
    }

}
