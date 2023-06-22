package com.example.code.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.UserDTO;
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
    public List<PostDTO> getProfile(int userId) {
        List<PostDTO> listPostDTO = userDao.getProFileByID(userId);
        return getBase64Post(listPostDTO);
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

    public void createComment(String content,int userID,int postID) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setContent(content);
        commentDto.setId(postID);
        commentDto.setUserID(userID);
        commentDao.insertComment(commentDto);
    }
    public void updateComment(String content, int commentID, int userID) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setContent(content);
        commentDto.setId(commentID);
        commentDto.setUserID(userID);

        commentDao.updateComment(commentDto);
    }

    public void deleteComment(int postID, int userID) {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(postID);
        commentDto.setUserID(userID);
        commentDao.deleteComment(commentDto);
    }

}