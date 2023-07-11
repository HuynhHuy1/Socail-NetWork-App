package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("following")
    ResponseEntity<ResponseDTO> getPostFollowing(@RequestAttribute("userID") int id) {
        List<PostDTO> postDTO = postService.getPostFollowingDtos(id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Success", " Lấy thành công danh sách ", postDTO));
    }

    @PostMapping()
    ResponseEntity<ResponseDTO> uploadPost(@RequestAttribute("userID") int id,
                                           @RequestParam(value = "Images",required =  false) MultipartFile[] images,
                                           @RequestParam(value = "Content",required = false) String content) {
        if(images == null && (content == null || content.equals(""))){
            return ResponseEntity.ok().body(new ResponseDTO("Failed","Image or Content required",""));
        }
        postService.insertPost(content, images, id);
        return ResponseEntity.ok().body(
                new ResponseDTO("Success", "Upload bài viết thành công", ""));
    }

    @PutMapping("{id}")
    ResponseEntity<ResponseDTO> updatePost(@RequestParam(value = "Images", required = false) MultipartFile[] images,
                                           @RequestParam(value = "Content", required = false) String content,
                                           @PathVariable("id") int statusId,
                                           @RequestAttribute("userID") int userID) {
        if(images == null && (content == null || content.equals(""))){
            return ResponseEntity.ok().body(new ResponseDTO("Failed","Image or Content required",""));
        }                                            
        ResponseDTO responseDTO = postService.updatePost(images, content, statusId, userID);
        if(responseDTO.getStatus().equals("Success"))
        {
            return ResponseEntity.ok().body(responseDTO);
        } 
        else{
            return ResponseEntity.ok().body(responseDTO);
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<ResponseDTO> deletePost(@PathVariable("id") int id, @RequestAttribute("userID") int userID) {
        ResponseDTO responseDTO = postService.deletePost(id, userID);
        if(responseDTO.getStatus().equals("Success")){
            return ResponseEntity.ok().body(responseDTO);
        }
        else{
            return ResponseEntity.ok().body(responseDTO);
        }
    }
}
