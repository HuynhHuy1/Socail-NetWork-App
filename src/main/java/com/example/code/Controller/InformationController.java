package com.example.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.service.PostService;
import com.example.code.service.UserService;

@RestController
@RequestMapping("api/information")
public class InformationController {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @PutMapping()
    public ResponseEntity<ResponseDTO> updateUser(@RequestAttribute("userID") int userID, String UserName,
            String Avatar,
            String UserAddress, String UserPhone, String Email, String PASSWORD) {
        try {
            userService.updateUser(UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD, userID);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Thay đổi thông tin thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO("False", "Thay đổi thông tin thất bại", ""));
        }
    }
    @PutMapping("password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestAttribute("userID") int userID,
            @RequestParam("OldPassword") String oldPassWord, @RequestParam("Password") String passWord) {
        try {
            if (userService.changePassword(userID, oldPassWord, passWord)) {
                return ResponseEntity.ok().body(new ResponseDTO("True", "Thay đổi password thành công", ""));
            } else {
                return ResponseEntity.ok().body(new ResponseDTO("False", "Password không đúng", ""));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO("False", "Thay đổi password thất bại", ""));
        }
    }
}
