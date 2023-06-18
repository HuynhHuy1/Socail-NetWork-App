package com.example.code.Service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.DAO.PostDao;
import com.example.code.DAO.UserDao;
import com.example.code.DTO.PostDTO;
import com.example.code.Model.User;
import com.example.code.middleware.Authorization;

@Service
public class PostService {
    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;
    @Autowired
    Authorization author;

    
    public List<PostDTO> getPost(String token){
        User user = author.parseToken(token);
        int id = user.getUserID();
        return postDao.getPostFriend(id);
    }

    public void insertPost(Map<String,String> mapPost, String token){
        String content = mapPost.get("Content");
        String image = mapPost.get("Image");
        String video = mapPost.get("Video");
        int id = author.parseToken(token).getUserID();
        postDao.insertPost(content, image, video, id);
    }
    
    public void updatePost(Map<String,String> mapPost,String token){
        String content = mapPost.get("Content");
        String image = mapPost.get("Image");
        String video = mapPost.get("Video");
        int id = author.parseToken(token).getUserID();
        postDao.updatePost(content, image, video,id);
    }
}
