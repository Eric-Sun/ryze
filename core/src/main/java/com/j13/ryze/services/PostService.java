package com.j13.ryze.services;

import com.j13.ryze.daos.ReplyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    ReplyDAO replyDAO;

    public int replyCount(int postId) {
        int count = replyDAO.replyCount(postId);
        return count;
    }
}
