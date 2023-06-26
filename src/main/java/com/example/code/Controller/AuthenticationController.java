package com.example.code.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.AuthenticationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody @Valid UserDTO userDto) {
        String token = authenticationService.signUp(userDto);
        logger.info(userDto.getName() + "Sign up");
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Đăng kí thành công", token));
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO userDto) {
        String token = authenticationService.login(userDto);
        logger.info(userDto.getName() + "Login");
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Đăng nhập thành công", token));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody @Email Map<String, String> mapEmail) {
        String email = mapEmail.get("email");
        authenticationService.sendConfirmationEmail(email);
        logger.info("Send email to " + email);
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Đã gửi mail xác nhận", ""));
    }

    @PostMapping("check-key-number")
    public ResponseEntity<ResponseDTO> checkKeyNumber(@RequestBody Map<String, Integer> mapKeyNumber) {
        int keyNumber = mapKeyNumber.get("keyNumber");
        String tokenResetPassword = authenticationService.generateTokenResetPassword(keyNumber);
        logger.info("Send token reset password : " + keyNumber) ;
        return ResponseEntity.ok().body(new ResponseDTO("Success", "Thành công", tokenResetPassword));
    }

    @PostMapping("reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody Map<String, String> mapPassword,
            @RequestAttribute("keyNumber") int keyNumber) {
            String password = mapPassword.get("password");  
            authenticationService.resetPassword(keyNumber, password);
            logger.info("Reset password : " + password) ;
            return ResponseEntity.ok().body(new ResponseDTO("True", "Cập nhật mậu khẩu thành công", ""));
    }
}
