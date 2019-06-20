package com.j13.ryze.services;

import com.j13.poppy.exceptions.CommonException;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.CollectionDAO;
import com.j13.ryze.vos.CollectionVO;
import com.j13.ryze.vos.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    @Autowired
    CollectionDAO collectionDAO;

    @Autowired
    PostService postService;

    /**
     * 添加收藏
     *
     * @param userId
     * @param postId
     * @return
     */
    public int collectionAdd(int userId, int postId) {
        boolean isExisted = collectionDAO.checkExist(userId, Constants.Collection.Type.POST, postId);
        if (isExisted) {
            throw new CommonException(ErrorCode.POST.COLLECT_IS_EXISTED);
        }
        return collectionDAO.add(userId, Constants.Collection.Type.POST, postId);
    }


    /**
     * 删除收藏
     *
     * @param userId
     * @param postId
     */
    public void collectionDelete(int userId, int postId) {
        boolean isExisted = collectionDAO.checkExist(userId, Constants.Collection.Type.POST, postId);
        if (!isExisted) {
            throw new CommonException(ErrorCode.POST.COLLECT_IS_DELETED);
        }
        collectionDAO.delete(userId, Constants.Collection.Type.POST, postId);
    }

    /**
     * 收藏列表
     *
     * @param userId
     * @param pageNum
     * @param size
     * @return
     */
    public List<CollectionVO> collectionList(int userId, int type, int pageNum, int size) {
        List<CollectionVO> collectionList = collectionDAO.list(userId, type, pageNum, size);
        for (CollectionVO collectionVO : collectionList) {
            int postId = collectionVO.getResourceId();
            PostVO postVO = postService.getSimplePost(postId);
            collectionVO.setResourceObject(postVO);
        }
        return collectionList;
    }


    /**
     * 查看这个帖子是否已经被对应的用户收藏过了
     *
     * @param userId
     * @param postId
     * @return
     */
    public boolean checkCollectionExisted(int userId, int postId) {
        return collectionDAO.checkExist(userId, Constants.Collection.Type.POST, postId);
    }


    public List<CollectionVO> queryCollectionsByResourceId(int resourceId, int type) {
        return collectionDAO.queryCollectionsByResourceId(resourceId, type);
    }

}
