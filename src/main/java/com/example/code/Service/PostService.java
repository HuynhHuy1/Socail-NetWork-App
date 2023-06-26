package com.example.code.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.UserDTO;
import com.example.code.exception.ExistException;
import com.example.code.exception.NullException;
import com.example.code.staticmessage.ErrorMessage;
import com.example.code.util.FileUitl;
import com.example.code.dao.CommentDao;
import com.example.code.dao.FriendshipDao;
import com.example.code.dao.LikeDao;
import com.example.code.dao.PostDao;
import com.example.code.dao.UserDao;

@Service
public class PostService {
    @Autowired
    PostDao postDao;
    @Autowired
    UserDao userDao;
    @Autowired
    LikeDao likeDao;
    @Autowired
    CommentDao commentDao;
    @Autowired
    FriendshipDao friendshipDao;
    @Autowired
    AuthorizationService author;
    @Autowired
    FileUitl fileUitl;
    @Autowired
    DataSource dataSource;

    public List<PostDTO> getPost(int userId) {
        if(userDao.getUserByID(userId) != null){
            List<PostDTO> listPostDTO = postDao.getPostFriend(userId);
            return getBase64PostFiles(listPostDTO);
        }
        else{
            throw new ExistException(ErrorMessage.USER_NOT_EXISTS);
        }
    }

    @Transactional
    public void insertPost(String content, MultipartFile[] images, int userID) {
            postDao.insertPost(content, userID);
            int idStatus = postDao.getLastInsertedPostID();
            List<String> pathFiles = fileToPathString(images);
            pathFiles.forEach((path) -> postDao.insertPostDetail(idStatus, path));
            TransactionAspectSupport.currentTransactionStatus().flush();
    }

    public void updatePost(MultipartFile[] images, String content, int postID) {
        List<String> listPathString = fileToPathString(images);
        postDao.updatePost(content, postID);
        postDao.deletePostDetail(postID);
        listPathString.forEach((pathString) -> {
            postDao.insertPostDetail(postID, pathString);
        });
        TransactionAspectSupport.currentTransactionStatus().flush();
    }

    public void deletePost(int id, int userID) {
        postDao.deletePost(id, userID);
    }

    public List<PostDTO> getProfile(int userId) {
        List<PostDTO> listPostDTO = userDao.getProFileByID(userId);
        return getBase64PostFiles(listPostDTO);
    }

    private List<PostDTO> getBase64PostFiles(List<PostDTO> listPostDTO) {
        List<String> listImageBase64 = new ArrayList<>();
        List<PostDTO> listBase64PostFiles = new ArrayList<>();
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
            listBase64PostFiles.add(newPost);
        });
        listBase64PostFiles.sort(Comparator.comparing(PostDTO::getTimeCreate).reversed());
        return listBase64PostFiles;
    }

    private List<String> fileToPathString(MultipartFile[] files) {
        List<String> listPath = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = fileUitl.addFileToStorage(file);
            listPath.add(path);
        }
        return listPath;
    }

    // like
    public List<UserDTO> getUserLike(int postID) {
        return likeDao.getUserLike(postID);
    }

    public void createLike(int userID, int postID) {
        LikeDTO likeDto = new LikeDTO(userID, postID);
        likeDao.insertLike(likeDto);
    }

    public void deleteLike(int postID, int userID) {
        likeDao.deleteLike(postID, userID);
    }

    public List<CommentDTO> getComment(int postID) {
        return commentDao.getComment(postID);
    }

    // comment

    public void createComment(String content, int userID, int postID) {
        if(content != null){
            CommentDTO commentDto = new CommentDTO();
            commentDto.setContent(content);
            commentDto.setId(postID);
            commentDto.setUserID(userID);
            commentDao.insertComment(commentDto);
        }
        else{
            throw new NullException(ErrorMessage.CONTENT_NULL);
        }

    }

    public void updateComment(String content, int commentID, int userID) {
        if(content != null){
            CommentDTO commentDto = new CommentDTO();
            commentDto.setContent(content);
            commentDto.setId(commentID);
            commentDto.setUserID(userID);
            commentDao.updateComment(commentDto);
        }
        else{
            throw new NullException(ErrorMessage.CONTENT_NULL);
        }
    }

    public void deleteComment(int postID, int userID) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(postID);
        commentDto.setUserID(userID);
        commentDao.deleteComment(commentDto);
    }

}