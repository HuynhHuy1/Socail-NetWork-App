package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/posts/{post-id}/likes")
public class LikeController {

    @Autowired
    PostService postService;

    @GetMapping("users-liked")
    ResponseEntity<ResponseDTO> getLike(@PathVariable("post-id") int postID) {
        try {
            List<UserDTO> listUserLike = postService.getUserLike(postID);
            return ResponseEntity.ok().body(
                    new ResponseDTO("True", "Lấy danh sách thành công", listUserLike));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseDTO("False", e.getStackTrace().toString(), ""));
        }
    }

    @PostMapping()
    ResponseEntity<ResponseDTO> postLike(@RequestAttribute("userID") int userID, 
                                         @PathVariable("post-id") int postID){
        try {
            postService.createLike(userID, postID);
            return ResponseEntity.ok().body(
                    new ResponseDTO("True", "Thích bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseDTO("False", e.getStackTrace().toString(), ""));
        }
    }

    @DeleteMapping()
    ResponseEntity<ResponseDTO> unLike(@RequestAttribute("userID") int userID, 
                                       @PathVariable("post-id") int postID){
        try {
            postService.deleteLike(postID, userID);
            return ResponseEntity.ok().body(
                    new ResponseDTO("True", "Huỷ Thích bài viết thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                    new ResponseDTO("False", e.getStackTrace().toString(), ""));
        }
    }

}