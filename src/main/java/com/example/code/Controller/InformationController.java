package com.example.code.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.PostService;
import com.example.code.service.UserService;

@RestController
@RequestMapping("api/information")
public class InformationController {
    
    private static final Logger logger = LogManager.getLogger(InformationController.class);

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @PutMapping()
    public ResponseEntity<ResponseDTO> updateUser(@RequestAttribute("userID") int userID,
            @RequestBody UserDTO userDto) {
        userDto.setId(userID);
        userService.updateUser(userDto);
        logger.info("Update User " + userID + " : " + userDto.toString());
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Thay đổi thông tin thành công", ""));
    }

    @PutMapping("password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestAttribute("userID") int userID,
        @RequestParam("OldPassword") String oldPassWord, @RequestParam("Password") String passWord) {
        userService.changePassword(userID, oldPassWord, passWord);
        logger.info("Replace password " + userID + " : " + oldPassWord + " with " + passWord);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Thay đổi password thành công", ""));
    }
}
