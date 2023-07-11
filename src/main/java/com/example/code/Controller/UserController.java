package com.example.code.controller;

import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.PostDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.PostService;
import com.example.code.service.UserService;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @GetMapping("{name}")
    public ResponseEntity<ResponseDTO> getUsersByName(@PathVariable("name") String userName) {
        if(userName ==null || userName.equals("")){
            return ResponseEntity.ok().body(new ResponseDTO("Failed","Username required",null));
        }
        List<UserDTO> listUser = userService.getUserByName(userName);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Lấy danh sách thành công", listUser));
    }
    
    @GetMapping("profile/{id}")
    public ResponseEntity<ResponseDTO> getUserProfileByID(@PathVariable("id") int userID) {
        UserDTO userDTO = postService.getProfile(userID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Lấy profile thành công", userDTO));
    }

    @GetMapping("profile/image/{id}")
    public ResponseEntity<ResponseDTO> getImageById(@PathVariable("id") int userID) {
        List<PostDTO> listImage = postService.getImageById(userID);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Lấy profile thành công",listImage));
    }
}
