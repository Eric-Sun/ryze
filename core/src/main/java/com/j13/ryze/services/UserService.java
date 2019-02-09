package com.j13.ryze.services;

import com.j13.ryze.daos.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserDAO userDAO;
    public String getNickName(int userId){
        return userDAO.getNickName(userId);
    }
}
