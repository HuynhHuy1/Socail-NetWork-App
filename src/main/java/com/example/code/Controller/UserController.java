package com.example.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.PostDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.PostService;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    PostService postService;

    @GetMapping("{name}")
    public ResponseEntity<ResponseDTO> getUsersByName(@PathVariable("name") String userName) {
        try {
            List<UserDTO> listUser = postService.getUserByName(userName);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Lấy danh sách thành công", listUser));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseDTO("True", "Lấy danh sách thất bại", ""));
        }
    }

    @GetMapping("profile/{id}")
    public ResponseEntity<ResponseDTO> getUserProfileByID(@PathVariable("id") int userID) {
        try {
            List<PostDTO> listPost = postService.getProfile(userID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Lấy profile thành công", listPost));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ResponseDTO("True", "Lấy profile thất bại", ""));
        }
    }

}
