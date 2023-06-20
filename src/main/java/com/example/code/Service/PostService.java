package com.example.code.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.code.DAO.PostDao;
import com.example.code.DAO.UserDao;
import com.example.code.DTO.PostDTO;
import com.example.code.Util.FileUitl;
import com.example.code.middleware.Authorization;

@Service
public class PostService {
    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;
    @Autowired
    Authorization author;
    @Autowired
    FileUitl fileUitl;

    
    public List<PostDTO> getPost(int id){
        List<PostDTO> listPostDTO = getBase64Post(id);
        return listPostDTO;
    }

    public void insertPost(String content, MultipartFile[] images,int userID){
        try {
            postDao.insertPost(content, userID);
            int idStatus = postDao.getLastInsertedStatusID();
            List<String> pathFiles = fileToPathString(images);
            pathFiles.forEach((path) -> postDao.insertPostDetail(idStatus, path));;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updatePost(MultipartFile[] images,String content, int statusID){
        try {
            List<String> listPathString = fileToPathString(images);
            postDao.updatePost(content, statusID);
            postDao.deletePostDetail(statusID);
            listPathString.forEach((pathString) ->{
                postDao.insertPostDetail(statusID,pathString);
            });            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deletePost(int id){
        postDao.deletePost(id);
    }
    private List<PostDTO> getBase64Post(int userID){
        List<PostDTO> listPost = postDao.getPostFriend(userID);
        List<String> listImageBase64 = new ArrayList<>();
        List<PostDTO> listPostBase64 = new ArrayList<>();
        listPost.forEach( (post) -> {
            List<String> listImage = postDao.getPostDetail(post.getStatusID());
            listImage.forEach((image) -> {
                String imageBase64 = fileUitl.readFile(image);
                listImageBase64.add(imageBase64);
            });
            PostDTO newPost = new PostDTO();
            newPost.setStatusID(post.getStatusID());
            newPost.setUserName(post.getUserName());
            newPost.setContent(post.getContent());
            newPost.setImage(listImageBase64);
            newPost.setLikeCount(post.getLikeCount());
            newPost.setCommentCount(post.getCommentCount());
            listPostBase64.add(newPost);
        });
        return listPostBase64; 
    }
    private List<String> fileToPathString(MultipartFile[] files){
        List<String> listPath = new ArrayList<>();
      for (MultipartFile file : files) {
            String path =    fileUitl.addFileToStorage(file);
            listPath.add(path);
      }
        return listPath;
    }
}
