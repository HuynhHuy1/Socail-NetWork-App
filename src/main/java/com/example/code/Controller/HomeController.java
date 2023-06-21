package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.DTO.CommentDTO;
import com.example.code.DTO.Friend_Request;
import com.example.code.DTO.LikeDTO;
import com.example.code.DTO.PostDTO;
import com.example.code.Model.Response;
import com.example.code.Model.User;
import com.example.code.Service.InteractPostService;
import com.example.code.Service.PostService;
import com.example.code.Util.FileUitl;
import com.example.code.middleware.Authorization;

@RestController
@RequestMapping("Api/Home")
public class HomeController {
    @Autowired
    PostService homeService;
    @Autowired
    Authorization author;
    @Autowired
    InteractPostService interactPostService;

    @GetMapping("PostFriend")
    ResponseEntity<Response> GetPostFriend(@RequestAttribute("userID") int id) {
        List<PostDTO> postDTO = homeService.getPost(id);
        return ResponseEntity.ok().body(
                new Response("True", " Lấy thành công danh sách ", postDTO));
    }
    
    @PostMapping("Post")
    ResponseEntity<Response> uploadPost(@RequestAttribute("userID") int id,
            @RequestParam("Images") MultipartFile[] images, @RequestParam("Content") String content) {
        try {
            homeService.insertPost(content, images, id);
            return ResponseEntity.ok().body(
                    new Response("Ok", "Upload bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                    new Response("Failed", "Lỗi xác thực ngừoi dùng", ""));
        }
    }

    @PutMapping("Post/{id}")
    ResponseEntity<Response> updatePost(@RequestParam("Images") MultipartFile[] images,
            @RequestParam("Content") String content, @PathVariable("id") int statusId) {
        try {
            homeService.updatePost(images, content, statusId);
            return ResponseEntity.ok().body(
                    new Response("True", "Update bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", "Không update thành công", ""));
        }
    }

    @DeleteMapping("Post/{id}")
    ResponseEntity<Response> deletePost(@PathVariable("id") int id, @RequestAttribute("userID") int userID) {
        try {
            homeService.deletePost(id, userID);
            return ResponseEntity.ok().body(
                    new Response("True", "Xoá bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @GetMapping("Like/{postId}")
    ResponseEntity<Response> getLike(@PathVariable("postId") int postID) {
        try {
            List<LikeDTO> listLike = interactPostService.getLike(postID);
            return ResponseEntity.ok().body(
                    new Response("True", "Lấy danh sách thành công", listLike));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @PostMapping("Like/{postId}")
    ResponseEntity<Response> like(@RequestAttribute("userID") int userID, @PathVariable("postId") int postID) {
        try {
            interactPostService.createILike(postID, userID);
            return ResponseEntity.ok().body(
                    new Response("True", "Thích bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @DeleteMapping("Like/{postId}")
    ResponseEntity<Response> unLike(@RequestAttribute("userID") int userID, @PathVariable("postId") int postID) {
        try {
            interactPostService.deleteLike(postID, userID);
            return ResponseEntity.ok().body(
                    new Response("True", "Huỷ Thích bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @GetMapping("Comment/{postID}")
    ResponseEntity<Response> getcomment(@PathVariable("postID") int postID) {
        try {
            List<CommentDTO> listComment = interactPostService.getComment(postID);
            return ResponseEntity.ok().body(
                    new Response("True", "Lấy danh sách bình luận thành công", listComment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @PostMapping("Comment/{postID}")
    ResponseEntity<Response> comment(@RequestParam("Content") String content, @RequestAttribute("userID") int userID,
            @PathVariable("postID") int postID) {
        try {
            interactPostService.createComment(content, userID, postID);
            return ResponseEntity.ok().body(
                    new Response("True", "Bình luận thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @PutMapping("Comment/{commentID}")
    ResponseEntity<Response> updateComment(@RequestParam("Content") String content,
            @PathVariable("commentID") int commmentID, @RequestAttribute("userID") int userID) {
        try {
            interactPostService.updateComment(content, commmentID, userID);
            return ResponseEntity.ok().body(
                    new Response("True", "Sửa bình luận thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @DeleteMapping("Comment/{commentID}")
    ResponseEntity<Response> deleteComment(@PathVariable("commentID") int commentID,
            @RequestAttribute("userID") int userID) {
        try {
            interactPostService.deleteComment(commentID, userID);
            return ResponseEntity.ok().body(
                    new Response("True", "Xoá bình luận thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new Response("False", e.getStackTrace().toString(), ""));
        }
    }

    @PostMapping("test")
    public String test(@PathVariable("file") MultipartFile file) {
        FileUitl fileUitl = new FileUitl();
        return fileUitl.addFileToStorage(file);
    }

    @GetMapping("test/{image}")
    public ResponseEntity<?> test1(@PathVariable("image") String image) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new FileUitl().readFile(image));
    }

    @GetMapping("User/{UserName}")
    public ResponseEntity<Response> getUserByName(@PathVariable("UserName") String userName) {
        try {
            List<User> listUser = homeService.getUserByName(userName);
            return ResponseEntity.ok().body(new Response("True", "Lấy danh sách thành công", listUser));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response("True", "Lấy danh sách thất bại", ""));
        }

    }

    @GetMapping("Profile/{UserID}")
    public ResponseEntity<Response> getProfileByID(@PathVariable("UserID") int userID) {
        try {
            List<PostDTO> listPostDTO = homeService.getProfile(userID);
            return ResponseEntity.ok().body(new Response("True", "Lấy profile thành công", listPostDTO));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response("True", "Lấy profile thất bại", ""));
        }
    }

    @GetMapping("FriendRequest")
    public ResponseEntity<Response> getRequestFriend(@RequestAttribute("userID") int userID) {
        try {
            List<Friend_Request> listRequest = homeService.getFriendRequest(userID);
            return ResponseEntity.ok()
                    .body(new Response("True", "Lấy danh sách lời mời kết bạn thành công", listRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new Response("False", "Lấy danh sách lời mời kết bạn thất bại", ""));
        }
    }

    @PostMapping("FriendRequest/{UserSendID}")
    public ResponseEntity<Response> addFriend(@RequestAttribute("userID") int userID,
            @PathVariable("UserSendID") int userSendID) {
        try {
            homeService.addFriend(userID, userSendID);
            return ResponseEntity.ok().body(new Response("True", "thêm bạn bè thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("False", "thêm bạn bè thất bại", ""));
        }
    }

    @PutMapping("FriendRequest/{UserSendID}")
    public ResponseEntity<Response> updateFriendRequest(@RequestAttribute("userID") int userID,
            @PathVariable("UserSendID") int userSendID, @RequestParam("Status") int status) {
        try {
            homeService.updateStatusFriendRequest(userID, userSendID, status);
            return ResponseEntity.ok().body(new Response("True", "Phản hồi thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response("False", "Phản hồi thất bại", ""));
        }
    }

    @DeleteMapping("Friend/{FriendID}")
    public ResponseEntity<Response> deleteFriend(@RequestAttribute("userID") int userID,
            @PathVariable("FriendID") int friendID) {
        try {
            homeService.deleteFriend(userID, friendID);
            return ResponseEntity.ok().body(new Response("True", "Xoá bạn thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("False", "Lấy danh sách bạn thất bại", ""));
        }
    }

    @GetMapping("Friend")
    public ResponseEntity<Response> getFriend(@RequestAttribute("userID") int userID) {
        try {
            List<User> listFriend = homeService.getFriend(userID);
            return ResponseEntity.ok().body(new Response("True", "Lấy danh sách bạn thành công", listFriend));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("False", "Lấy danh sách bạn thất bại", ""));
        }
    }

    @PutMapping("UserInfo")
    public ResponseEntity<Response> updateUser(@RequestAttribute("userID") int userID, String UserName, String Avatar,
            String UserAddress, String UserPhone, String Email, String PASSWORD) {
        try {
            homeService.updateUser(UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD, userID);
            return ResponseEntity.ok().body(new Response("True", "Thay đổi thông tin thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("False", "Thay đổi thông tin thất bại", ""));
        }
    }

    @PutMapping("ChangePassword")
    public ResponseEntity<Response> changePassword(@RequestAttribute("userID") int userID,
            @RequestParam("OldPassword") String oldPassWord, @RequestParam("Password") String passWord) {
        try {
            if (homeService.changePassword(userID, oldPassWord, passWord)) {
                return ResponseEntity.ok().body(new Response("True", "Thay đổi password thành công", ""));
            } else {
                return ResponseEntity.ok().body(new Response("False", "Password không đúng", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response("False", "Thay đổi password thất bại", ""));
        }
    }
}
