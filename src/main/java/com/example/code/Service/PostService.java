package com.example.code.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.FriendShipDTO;
import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.UserDTO;
import com.example.code.util.FileUitl;
import com.example.code.dao.InteractPostDAO;
import com.example.code.dao.PostDao;
import com.example.code.dao.UserDao;

@Service
public class PostService {
    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;
    @Autowired
    AuthorizationService author;
    @Autowired
    FileUitl fileUitl;
    @Autowired
    InteractPostDAO interactDAO;

    public List<PostDTO> getPost(int id) {
        List<PostDTO> listPostDTO = postDao.getPostFriend(id);
        return getBase64Post(listPostDTO);
    }

    public void insertPost(String content, MultipartFile[] images, int userID) {
        try {
            postDao.insertPost(content, userID);
            int idStatus = postDao.getLastInsertedPostID();
            List<String> pathFiles = fileToPathString(images);
            pathFiles.forEach((path) -> postDao.insertPostDetail(idStatus, path));
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePost(MultipartFile[] images, String content, int postID) {
        try {
            List<String> listPathString = fileToPathString(images);
            postDao.updatePost(content, postID);
            postDao.deletePostDetail(postID);
            listPathString.forEach((pathString) -> {
                postDao.insertPostDetail(postID, pathString);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePost(int id, int userID) {
        postDao.deletePost(id, userID);
    }

    public List<UserDTO> getUserByName(String userName) {
        List<UserDTO> users = userDao.getUsersByName(userName);
        return users;
    }

    public List<PostDTO> getProfile(int userId) {
        List<PostDTO> listPostDTO = postDao.getProFileByID(userId);
        return getBase64Post(listPostDTO);

    }

    public List<FriendShipDTO> getFriendRequest(int user2ID) {
        List<FriendShipDTO> listRequest = postDao.getFriendRequest(user2ID);
        ;
        return listRequest;
    }

    public void addFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);
        postDao.insertFriend(friendShipDto);
    }

    public void updateStatusFriendRequest(int userID, int user2ID) {
        FriendShipDTO friendShipDTO = new FriendShipDTO();
        friendShipDTO.setUserID(userID);
        friendShipDTO.setUser2ID(user2ID);
        postDao.updateStatusFriendRequest(friendShipDTO);
    }

    public List<UserDTO> getFriend(int id) {
        return postDao.getFriend(id);
    }

    public void deleteFriend(int userID, int user2ID) {
        FriendShipDTO friendShipDto = new FriendShipDTO();
        friendShipDto.setUserID(userID);
        friendShipDto.setUser2ID(user2ID);
        postDao.deleteFriend(friendShipDto);
    }

    public void updateUser(String UserName, String Avatar, String UserAddress, String UserPhone, String Email,
            String PASSWORD, int id) {
        UserDTO user = new UserDTO(id, UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD);
        postDao.updateUser(user);
    }

    private List<PostDTO> getBase64Post(List<PostDTO> listPostDTO) {
        List<String> listImageBase64 = new ArrayList<>();
        List<PostDTO> listPostBase64 = new ArrayList<>();
        listPostDTO.forEach((post) -> {
            List<String> listImage = postDao.getPostDetail(post.getId());
            listImage.forEach((image) -> {
                String imageBase64 = fileUitl.readFile(image);
                listImageBase64.add(imageBase64);
            });
            PostDTO newPost = new PostDTO();
            newPost.setId(post.getId());
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

    private List<String> fileToPathString(MultipartFile[] files) {
        List<String> listPath = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = fileUitl.addFileToStorage(file);
            listPath.add(path);
        }
        return listPath;
    }

    public boolean changePassword(int userId, String oldPassWord, String passWord) {
        UserDTO user = userDao.getUserByID(userId);
        if (user.getPassword().equals(oldPassWord)) {
            postDao.updatePassWord(passWord, userId);
            return true;
        }
        return false;
    }

    public List<LikeDTO> getLike(int postID) {
        return postDao.getLike(postID);
    }

    public void createILike(int userID, int postID) {
        LikeDTO likeDto = new LikeDTO(userID,postID);
        postDao.insertLike(likeDto);
    }

    public void deleteLike(int postID, int userID) {
        postDao.deleteLike(postID, userID);
    }

    public List<CommentDTO> getComment(int postID) {
        return postDao.getComment(postID);
    }

    public void createComment(String content,int userID,int postID) {
        PostDTO postDto = new PostDTO();
        postDto.setContent(content);
        postDto.setId(postID);
        postDto.setUserID(userID);
        postDao.insertComment(postDto);
    }
    public void updateComment(String content, int commentID, int userID) {
        postDao.updateComment(content, commentID, userID);
    }

    public void deleteComment(int postID, int userID) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(postID);
        commentDto.setUserID(userID);
        postDao.deleteComment(postID, userID);
    }
}