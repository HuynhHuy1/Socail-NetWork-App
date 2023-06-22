package com.example.code.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.AuthenResponseDTO;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody @Valid UserDTO userDto) {
        AuthenResponseDTO authenResponse = authenticationService.signUp(userDto);
        return authenResponse.getStatus() ? ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                new ResponseDTO("Success", "Đăng ký thành công", authenResponse.getData()))
                : ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                        new ResponseDTO("Failed", "Đăng ký không thành công", ""));
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid UserDTO userDto) {
        AuthenResponseDTO authenResponse = authenticationService.login(userDto);
        return authenResponse.getStatus() ? ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                new ResponseDTO("Success", "Đăng nhập thành công", authenResponse.getData()))
                : ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                        new ResponseDTO("Failed", "Đăng nhập không thành công", ""));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody Map<String, String> mapEmail) {
        String email = mapEmail.get("email");
        if (authenticationService.isEmailExist(email)) {
            authenticationService.sendConfirmationEmail(email);
            return ResponseEntity.ok().body(new ResponseDTO("Success", "Thành công", ""));
        } else
            return ResponseEntity.ok().body(new ResponseDTO("Failed", "Thất bại", ""));
    }

    @PostMapping("check-key-number")
    public ResponseEntity<ResponseDTO> checkKeyNumber(@RequestBody Map<String, Integer> mapKeyNumber) {
        int keyNumber = mapKeyNumber.get("keyNumber");
        if (authenticationService.isKeyNumberExist(keyNumber)) {
            String token = authenticationService.generateTokenResetPassword(keyNumber);
            return ResponseEntity.ok().body(new ResponseDTO("Success", "Thành công", token));
        } else
            return ResponseEntity.ok().body(new ResponseDTO("Failed", "Thất bại", ""));
    }

    @PostMapping("reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody Map<String, String> mapPassword,
            @RequestAttribute("keyNumber") int keyNumber) {
        String password = mapPassword.get("password");
        try {
            authenticationService.resetPassWord(keyNumber, password);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Cập nhật mậu khẩu thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("False", "Cập nhật mật khẩu thất bại", ""));
        }
    }
}
