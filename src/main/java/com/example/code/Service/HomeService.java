package com.example.code.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.DAO.PostDao;
import com.example.code.DAO.UserDao;
import com.example.code.DTO.PostDTO;

@Service
public class HomeService {
    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;strictfp
    
    public List<PostDTO> getPost(String email){
        int id = getUserIDByEmail(email);
        return postDao.getPostFriend(id);
    }


    private int getUserIDByEmail(String email){
        return userDao.getIDByEmail(email);
    }
}
