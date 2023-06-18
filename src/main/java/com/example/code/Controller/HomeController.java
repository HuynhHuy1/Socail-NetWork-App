package com.example.code.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.code.DTO.PostDTO;
import com.example.code.Service.PostService;
import com.example.code.middleware.Authorization;
@RestController
@RequestMapping("Home")
public class HomeController {
    @Autowired
    PostService homeService;
    @Autowired
    Authorization author;
    @GetMapping("PostFriend")
    ResponseEntity<?> GetPostFriend (@RequestHeader String token){
        if(author.isValidUser(token)){
            List<PostDTO> postDTO = homeService.getPost(token);
            return ResponseEntity.ok().body(postDTO);
        }
        return ResponseEntity.status(401).body("Không truy cập được");
    }
    @PostMapping("Post")
    ResponseEntity<?> uploadPost(@RequestHeader String token,@RequestBody Map<String,String> mapPost){
        if(author.isValidUser(token)){
            homeService.insertPost(mapPost,token);
            return ResponseEntity.ok().body("{\"message\": \"Đã thêm bài viết\"}");
        }
        return ResponseEntity.status(401).body("Không truy cập được");

    }
    @PutMapping("Post")
    ResponseEntity<?> updatePost(@RequestHeader String token,@RequestBody Map<String,String> mapPost){
        if(author.isValidUser(token)){
            homeService.updatePost(mapPost, token);
            return ResponseEntity.ok().body("{\"message\": \"Đã sửa bài viết\"}");
        }
        return ResponseEntity.status(401).body("Không truy cập được");

    }
    
}
