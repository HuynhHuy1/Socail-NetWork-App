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

import com.example.code.dto.ResponseDTO;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/posts/{post-id}/comments")
public class CommentController {

        @Autowired
        PostService postService;

        @GetMapping()
        ResponseEntity<ResponseDTO> getCommentByPostID(@PathVariable("post-id") int postID) {
                ResponseDTO responseDTO = postService.getComment(postID);
                if(responseDTO.getStatus().equals("Failed")){
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
        }

        @PostMapping()
        ResponseEntity<ResponseDTO> uploadComment(@RequestParam( value =  "Content",required = false) String content,
                                                  @RequestAttribute("userID") int userID,
                                                  @PathVariable("post-id") int postID) {
                if(content.equals("") || content == null ){
                        return ResponseEntity.badRequest().body(
                                new ResponseDTO("Failed", "Content required", null));
                }
                ResponseDTO responseDTO =  postService.createComment(content, userID, postID);
                if(responseDTO.getStatus().equals("Failed")){
                        return ResponseEntity.status(404).body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
        }

        @PutMapping("{comment-id}")
        ResponseEntity<ResponseDTO> updateComment(@RequestParam("Content") String content,
                                                  @RequestAttribute("userID") int userID,
                                                  @PathVariable("comment-id") int commentID) {
                ResponseDTO responseDTO = postService.updateComment(content, commentID, userID);
                if(responseDTO.getStatus().equals("Failed")){
                        return ResponseEntity.status(404).body(responseDTO);
                }
                return ResponseEntity.ok().body(responseDTO);
        }

        @DeleteMapping("{comment-id}")
        ResponseEntity<ResponseDTO> deleteComment(@PathVariable("comment-id") int commentID,
                                                  @RequestAttribute("userID") int userID) {
                ResponseDTO responseDTO = postService.deleteComment(commentID, userID);
                if(responseDTO.getStatus().equals("Failed")){
                                return ResponseEntity.status(404).body(responseDTO);
                }
                return ResponseEntity.ok().body(
                                new ResponseDTO("Success", "Xoá bình luận thành công", ""));
        }
}
