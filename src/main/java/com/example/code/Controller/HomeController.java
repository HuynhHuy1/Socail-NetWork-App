package com.example.code.Controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.DTO.PostDTO;
import com.example.code.Model.Response;
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
    @GetMapping("PostFriend")
    ResponseEntity<Response> GetPostFriend (@RequestAttribute("userID") int id){
                List<PostDTO> postDTO =  homeService.getPost(id);
                return ResponseEntity.ok().body(
                    new Response("True", " Lấy thành công danh sách ", postDTO)
                );  
    }
    @PostMapping("Post")
    ResponseEntity<Response> uploadPost(@RequestAttribute("userID") int id, @RequestParam("Images") MultipartFile[] images,@RequestParam("Content") String content){
        try {
            homeService.insertPost(content,images,id);
            return ResponseEntity.ok().body(
                new Response("Ok", "Upload bài viết thành công", "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                new Response("Failed","Lỗi xác thực ngừoi dùng","")
            );
        }
    }
    @PutMapping("Post/{id}")
    ResponseEntity<Response> updatePost(@RequestParam("Images") MultipartFile[] images,@RequestParam("Content") String content,@PathVariable("id") int statusId){
        try {
            homeService.updatePost(images,content,statusId);
            return ResponseEntity.ok().body(
                new Response("True", "Update bài viết thành công", "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body(
                new Response("False", "Không update thành công", "")
            );
        }
    }
    @DeleteMapping("Post/{id}")
    ResponseEntity<Response> deletePost(@RequestHeader String token,@PathVariable int id){
            try {
                homeService.deletePost(id);
                return ResponseEntity.ok().body(
                    new Response("True", "Xoá bài viết thành công", "")
                );
            } catch(Exception e) {
               return ResponseEntity.status(HttpStatus.FOUND).body(
                new Response("False", e.getStackTrace().toString(), ""));
            }   
    }
    @PostMapping("test")
    public String test(@PathVariable("file") MultipartFile file){
        FileUitl fileUitl = new FileUitl();
        return fileUitl.addFileToStorage(file); 
    }
    @GetMapping("test/{image}" )
    public ResponseEntity<?> test1(@PathVariable("image") String image){
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(new FileUitl().readFile(image));
    }
    
}
