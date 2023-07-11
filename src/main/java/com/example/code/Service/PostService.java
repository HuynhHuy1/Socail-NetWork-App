package com.example.code.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.dto.CommentDTO;
import com.example.code.dto.LikeDTO;
import com.example.code.dto.PostDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
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
        List<PostDTO> listPostDTO = postDao.getPostFriend(userId);
        return getBase64PostFiles(listPostDTO, userId);
    }

    public List<PostDTO> getPostFollowingDtos(int userId) {
        List<PostDTO> listPostDTO = postDao.getPostFollowingDtos(userId);
        return getBase64PostFiles(listPostDTO, userId);
    }

    @Transactional
    public void insertPost(String content, MultipartFile[] images, int userID) {
        postDao.insertPost(content, userID);
        int idStatus = postDao.getLastInsertedPostID();
        if (images != null) {
            List<String> pathFiles = fileToPathString(images);
            pathFiles.forEach(path -> postDao.insertPostDetail(idStatus, path));
        }
    }

    @Transactional
    public ResponseDTO updatePost(MultipartFile[] images, String content, int postID, int userID) {
        PostDTO postDTO = postDao.getPostByID(postID);
        if (postDTO == null)
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        if (postDTO.getUserID() != userID)
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_OF_USER, null);
        int rowUpdate = postDao.updatePost(content, postID);
        int rowDelete = postDao.deletePostDetail(postID);
        if (rowDelete == 0 || rowUpdate == 0)
            throw new RuntimeException();
        if (images != null) {
            List<String> listPathString = fileToPathString(images);
            listPathString.forEach(pathString -> {
                postDao.insertPostDetail(postID, pathString);
            });
        }
        return new ResponseDTO("Success", "Sửa bài viết thành công", null);
    }

    public ResponseDTO deletePost(int postId, int userID) {
        PostDTO postDTO = postDao.getPostByID(postId);
        if (postDTO == null)
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        if (postDTO.getUserID() != userID)
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_OF_USER, null);
        postDao.deletePost(postId);
        return new ResponseDTO("Success", "Xoá bài viết thành công", null);
    }

    public UserDTO getProfile(int userId) {
        UserDTO userProfileDTO = userDao.getProfileUserDTO(userId);
        userProfileDTO.setAvatar(new FileUitl().readFile(userProfileDTO.getAvatar()));
        return userProfileDTO;
    }

    private List<PostDTO> getBase64PostFiles(List<PostDTO> listPostDTO, int userID) {
        List<PostDTO> listBase64PostFiles = new ArrayList<>();
        listPostDTO.forEach((post) -> {
            String Image = postDao.getPostDetail(post.getPostId());
            String imageBase64 = fileUitl.readFile(Image);
            PostDTO newPost = new PostDTO();
            newPost.setPostId(post.getPostId());
            newPost.setUserName(post.getUserName());
            newPost.setContent(post.getContent());
            newPost.setImage(imageBase64);
            newPost.setTimeCreate(post.getTimeCreate());
            String timeFormat = fileUitl.formatTimestamp(post.getTimeCreate().toString());
            newPost.setTimeFormatString(timeFormat);
            newPost.setLikeCount(post.getLikeCount());
            newPost.setCommentCount(post.getCommentCount());
            newPost.setUserID(post.getUserID());
            Boolean isBoolean = isUserLike(userID, userID);
                newPost.setStateLike(isUserLike(userID, post.getPostId()));
            String avatarBase64 = fileUitl.readFile(post.getAvatar());
            newPost.setAvatarBase64(avatarBase64);
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

    public ResponseDTO getUserLike(int postID) {
        PostDTO postDTO = postDao.getPostByID(postID);
        if (postDTO == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        }
        List<UserDTO> listLikeDTO = likeDao.getUserLike(postID);
        return new ResponseDTO("Success", "Lấy danh sách thành công", listLikeDTO);
    }

    public ResponseDTO Like(int userID, int postID) {
        PostDTO postDTO = postDao.getPostByID(postID);
        if (postDTO == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        }
        if (likeDao.getLikeByUserIDAndPostID(postID, userID) != null) {
            likeDao.deleteLike(postID, userID);
            return new ResponseDTO("Success", "Thích bài viết thành công", null);
        }
        LikeDTO likeDto = new LikeDTO(userID, postID);
        likeDao.insertLike(likeDto);
        return new ResponseDTO("Success", "Thích bài viết thành công", null);
    }

    public ResponseDTO createLike(int userID, int postID) {
        PostDTO postDTO = postDao.getPostByID(postID);
        if (postDTO == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        }
        if (likeDao.getLikeByUserIDAndPostID(postID, userID) != null) {
            return new ResponseDTO("Failed", ErrorMessage.LIKE_DUPLICATE, null);
        }
        LikeDTO likeDto = new LikeDTO(userID, postID);
        likeDao.insertLike(likeDto);
        return new ResponseDTO("Success", "Thích bài viết thành công", null);
    }

    public ResponseDTO deleteLike(int postID, int userID) {
        PostDTO postDTO = postDao.getPostByID(postID);
        if (postDTO == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        }
        if (likeDao.getLikeByUserIDAndPostID(postID, userID) == null) {
            return new ResponseDTO("Failed", ErrorMessage.LIKE_NOT_EXIST, null);
        }
        likeDao.deleteLike(postID, userID);
        return new ResponseDTO("Success", "Huỷ thích bài viết thành công", null);
    }

    // comment
    public ResponseDTO getComment(int postID) {
        PostDTO postDto = postDao.getPostByID(postID);
        if (postDto == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, null);
        }
        List<CommentDTO> listComment = commentDao.getComment(postID);
        listComment.forEach( (comment) -> {
            String avatarBase64 = new FileUitl().readFile(comment.getAvatar());
            comment.setAvatar(avatarBase64);
        });
        return new ResponseDTO("Success", "Lấy bài đăng thành công", listComment);
    }

    public ResponseDTO createComment(String content, int userID, int postID) {
        PostDTO postDto = postDao.getPostByID(postID);
        if (postDto == null) {
            return new ResponseDTO("Failed", ErrorMessage.POST_NOT_EXIST, postDto);
        }
        CommentDTO commentDto = new CommentDTO();
        commentDto.setContent(content);
        commentDto.setPostId(postID);
        commentDto.setUserId(userID);
        commentDao.insertComment(commentDto);
        return new ResponseDTO("Success", "Bình luận thành công", null);
    }

    public ResponseDTO updateComment(String content, int commentID, int userID) {
        UserDTO userComment = commentDao.getUserIDByCommentID(commentID);
        if (userComment == null) {
            return new ResponseDTO("Failed", ErrorMessage.COMMENT_NOT_EXIST, null);
        } else if (userComment.getId() == userID) {
            CommentDTO commentDto = new CommentDTO();
            commentDto.setContent(content);
            commentDto.setId(commentID);
            commentDto.setUserId(userID);
            commentDao.updateComment(commentDto);
            return new ResponseDTO("Success", "Cập nhật bình luận thành công", null);
        }

        return new ResponseDTO("Failed", ErrorMessage.COMMENT_NOT_OF_USER, null);
    }

    public ResponseDTO deleteComment(int commentID, int userID) {
        UserDTO userComment = commentDao.getUserIDByCommentID(commentID);
        if (userComment == null) {
            return new ResponseDTO("Failed", ErrorMessage.COMMENT_NOT_EXIST, null);
        }
        if (userComment.getId() == userID) {
            CommentDTO commentDto = new CommentDTO();
            commentDto.setId(commentID);
            commentDto.setUserId(userID);
            commentDao.deleteComment(commentDto);
            return new ResponseDTO("Success", "Xoá bình luận thành công", null);
        }
        return new ResponseDTO("Failed", ErrorMessage.COMMENT_NOT_OF_USER, null);
    }

    public boolean isUserLike(int userID, int postID) {
        LikeDTO likeDTO = postDao.getLikeDTO(postID, userID);
        return likeDTO != null;
    }

    public List<PostDTO> getImageById(int id){
        List<PostDTO> listImage = postDao.getImageById(id);
        listImage.forEach((image) ->{
            String imageBase64 = new FileUitl().readFile(image.getImage());
            image.setImage(imageBase64);
            String timeFormat = FileUitl.formatTimestamp(image.getTimeCreate().toString());
            image.setTimeFormatString(timeFormat);
        });
        return listImage ;
    }
}