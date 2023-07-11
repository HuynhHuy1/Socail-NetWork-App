package com.example.code.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/posts/{post-id}/likes")
public class LikeController {

    @Autowired
    PostService postService;

    @GetMapping()
    ResponseEntity<ResponseDTO> getLike(@PathVariable("post-id") int postID) {
        ResponseDTO responseDTO = postService.getUserLike(postID);
        if(responseDTO.getStatus().equals("Success")){
            return ResponseEntity.ok().body(responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("handle-like")
    ResponseEntity<ResponseDTO> Like(@RequestAttribute("userID") int userID,
            @PathVariable("post-id") int postID) {
                ResponseDTO responseDTO = postService.Like(userID,postID);
                if(responseDTO.getStatus().equals("Success")){
                    return ResponseEntity.ok().body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping()
    ResponseEntity<ResponseDTO> postLike(@RequestAttribute("userID") int userID,
            @PathVariable("post-id") int postID) {
                ResponseDTO responseDTO = postService.createLike(userID,postID);
                if(responseDTO.getStatus().equals("Success")){
                    return ResponseEntity.ok().body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping()
    ResponseEntity<ResponseDTO> unLike(@RequestAttribute("userID") int userID,
            @PathVariable("post-id") int postID) {
                ResponseDTO responseDTO = postService.deleteLike(postID,userID);
                if(responseDTO.getStatus().equals("Success")){
                    return ResponseEntity.ok().body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
    }
}
