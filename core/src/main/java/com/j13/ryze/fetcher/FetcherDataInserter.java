package com.j13.ryze.fetcher;

import com.j13.ryze.daos.FPostDAO;
import com.j13.ryze.daos.FReplyDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FetcherDataInserter {

    @Autowired
    FPostDAO fPostDAO;
    @Autowired
    FReplyDAO fReplyDAO;
    @Autowired
    PostService postService;
    @Autowired
    ReplyService replyService;
    @Autowired
    UserService userService;

    public void insertPost(){
        //插入一个post
        FPostVO fPostVO = fPostDAO.selectOneUninsertedPost();

        userService.randomMachineUser();

    }

    public void insertReplys(){
        //插入所有的已插入post中的1-10随机的reply
    }


}
