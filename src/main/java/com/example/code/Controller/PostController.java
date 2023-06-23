package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.code.dto.PostDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.service.AuthorizationService;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    AuthorizationService author;

    @GetMapping("friends")
    ResponseEntity<ResponseDTO> getPostFriend(@RequestAttribute("userID") int id) {
        List<PostDTO> postDTO = postService.getPost(id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Success", " Lấy thành công danh sách ", postDTO));
    }

    @PostMapping()
    ResponseEntity<ResponseDTO> uploadPost(@RequestAttribute("userID") int id,
            @RequestParam("Images") MultipartFile[] images, @RequestParam("Content") String content) {
        postService.insertPost(content, images, id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Ok", "Upload bài viết thành công", ""));
    }

    @PutMapping("{id}")
    ResponseEntity<ResponseDTO> updatePost(@RequestParam("Images") MultipartFile[] images,
            @RequestParam("Content") String content, @PathVariable("id") int statusId) {
        postService.updatePost(images, content, statusId);
        return ResponseEntity.ok().body(
                new ResponseDTO("Success", "Update bài viết thành công", ""));
    }

    @DeleteMapping("{id}")
    ResponseEntity<ResponseDTO> deletePost(@PathVariable("id") int id, @RequestAttribute("userID") int userID) {
        postService.deletePost(id, userID);
        return ResponseEntity.ok().body(
                new ResponseDTO("Success", "Xoá bài viết thành công", ""));
    }

}
