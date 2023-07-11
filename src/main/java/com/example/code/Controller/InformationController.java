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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ResponseDTO> updateUser(@RequestAttribute(value = "userID",required = false) int userID,
                                                  @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                                  @RequestParam(value = "userName", required = false) String userName,
                                                  @RequestParam(value = "address",required = false) String address,
                                                  @RequestParam(value = "phone",required = false) String phone ) {
        
        if(avatar == null && userName == null && address== null && phone==null) 
        {
            return ResponseEntity.ok().body(new ResponseDTO("Failed", "Request update null", null));
        }
        UserDTO userDto = new UserDTO();
        userDto.setName(userName);
        userDto.setAddress(address);
        userDto.setPhone(phone);
        userService.updateUser(userDto,userID,avatar);
        logger.info("Update User " + userID + " : " + userDto.toString());
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Thay đổi thông tin thành công", ""));
    }

    @PutMapping("password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestAttribute("userID") int userID,
            @RequestParam(value = "OldPassword",required = false) String oldPassWord, @RequestParam(value =  "Password",required =false) String passWord) {
        if(oldPassWord == null || passWord == null){
            return ResponseEntity.ok().body(new ResponseDTO("Failed", "Old password and password not null", null));
        }
        if(userService.changePassword(userID, oldPassWord, passWord)){
            logger.info("Replace password " + userID + " : " + oldPassWord + " with " + passWord);
            return ResponseEntity.ok().body(new ResponseDTO("Success", "Thay đổi password thành công", null));
        }
        return ResponseEntity.ok().body(new ResponseDTO("Failed", "Mật khẩu bị trùng", null));
    }
}
