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

import com.example.code.dto.CommentDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/posts/{post-id}/comments")
public class CommentController {

        @Autowired
        PostService postService;

        @GetMapping()
        ResponseEntity<ResponseDTO> getCommentByPostID(@PathVariable("post-id") int postID) {
                List<CommentDTO> listComment = postService.getComment(postID);
                return ResponseEntity.ok().body(
                                new ResponseDTO("Success", "Lấy danh sách bình luận thành công", listComment));
        }

        @PostMapping()
        ResponseEntity<ResponseDTO> uploadComment(@RequestParam("Content") String content,
                        @RequestAttribute("userID") int userID,
                        @PathVariable("postID") int postID) {
                postService.createComment(content, userID, postID);
                return ResponseEntity.ok().body(
                                new ResponseDTO("Success", "Bình luận thành công", ""));
        }

        @PutMapping("{comment-id}")
        ResponseEntity<ResponseDTO> updateComment(@RequestParam("Content") String content,
                        @RequestAttribute("userID") int userID,
                        @PathVariable("comment-id") int commmentID) {
                postService.updateComment(content, commmentID, userID);
                return ResponseEntity.ok().body(
                                new ResponseDTO("Success", "Sửa bình luận thành công", ""));
        }

        @DeleteMapping("{comment-id}")
        ResponseEntity<ResponseDTO> deleteComment(@PathVariable("comment-id") int commentID,
                        @RequestAttribute("userID") int userID) {
                return ResponseEntity.ok().body(
                                new ResponseDTO("Success", "Xoá bình luận thành công", ""));
        }
}
