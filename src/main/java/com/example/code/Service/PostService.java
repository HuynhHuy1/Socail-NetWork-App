package com.example.code.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.code.DAO.PostDao;
import com.example.code.DAO.UserDao;
import com.example.code.DTO.Friend_Request;
import com.example.code.DTO.PostDTO;
import com.example.code.Model.User;
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
        List<PostDTO> listPostDTO = postDao.getPostFriend(id);
        return getBase64Post(listPostDTO);
    }

    public void insertPost(String content, MultipartFile[] images,int userID){
        try {
            postDao.insertPost(content, userID);
            int idStatus = postDao.getLastInsertedPostID();
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
    public void deletePost(int id,int userID){
        postDao.deletePost(id,userID);
    }
    public List<User> getUserByName(String userName){
        List<User> users = userDao.getUsersByName(userName);
        return users;
    }

    public List<PostDTO>  getProfile(int userId){
        List<PostDTO> listPostDTO = postDao.getProFileByID(userId);
        return getBase64Post(listPostDTO);
        
    }

    public List<Friend_Request> getFriendRequest(int userSendID){
        List<Friend_Request> listRequest = postDao.getFriendRequest(userSendID);
;        return listRequest;
    }
    public void addFriend(int userID, int userSendID){
        postDao.insertFriends(userID,userSendID);
    }
    public void updateStatusFriendRequest(int userID, int userSendID, int Status ){
        postDao.updateStatusFriendRequest(userID,userSendID,Status);
    }
    public List<User> getFriend(int id){
        return postDao.getFriend(id);
    }
    public void deleteFriend(int userID, int friendID){
        postDao.deleteFriend(friendID, userID);
    }
    public void updateUser(String UserName, String Avatar, String UserAddress,  String UserPhone, String Email, String PASSWORD,int id){
        User user = new User(id, UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD);
        postDao.updateUser(user);
    }
    private List<PostDTO> getBase64Post(List<PostDTO> listPostDTO){
        List<String> listImageBase64 = new ArrayList<>();
        List<PostDTO> listPostBase64 = new ArrayList<>();
        listPostDTO.forEach( (post) -> {
            List<String> listImage = postDao.getPostDetail(post.getPostID());
            listImage.forEach((image) -> {
                String imageBase64 = fileUitl.readFile(image);
                listImageBase64.add(imageBase64);
            });
            PostDTO newPost = new PostDTO();
            newPost.setPostID(post.getPostID());
            newPost.setUserName(post.getUserName());
            newPost.setContent(post.getContent());
            newPost.setImage(listImageBase64);
            newPost.setTimeCreate(post.getTimeCreate());
            newPost.setLikeCount(post.getLikeCount());
            newPost.setCommentCount(post.getCommentCount());
            listPostBase64.add(newPost);
        });
        listPostBase64.sort(Comparator.comparing(PostDTO::getTimeCreate).reversed());
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


    public boolean changePassword(int userId, String oldPassWord, String passWord) {
        User user = userDao.getUserByID(userId);
        if(user.getPASSWORD().equals(oldPassWord)){
            postDao.updatePassWord(passWord,userId);
            return true;
        }
        return false;
    }
    
}
