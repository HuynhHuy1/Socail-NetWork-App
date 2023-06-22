package com.example.code.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.dto.ResponseDTO;
import com.example.code.service.AuthenticationService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authen;

    @PostMapping("signup")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody Map<String, String> userInfo) {
        String token = authen.signUp(userInfo);
        return token.equals("EmailError") ? ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .body(new ResponseDTO("Failed", "Email đã tồn tại", ""))
                : ResponseEntity.ok().body(new ResponseDTO("Ok", "Đăng ký thành công", token));
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDTO> login(@RequestBody Map<String, String> map) {
        String token = authen.login(map);
        if (token.equals("PasswordError") || token.equals("EmailError")) {
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                    new ResponseDTO("Failed", "Đăng nhập không thành công", ""));
        } else {
            return ResponseEntity.ok().body(
                    new ResponseDTO("Ok", "Đăng nhập thành công", token));
        }
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ResponseDTO> forgetPassword(@RequestParam("Email") String email) {
        return authen.sendKeyNumbe(email)
                ? ResponseEntity.ok().body(new ResponseDTO("True", "Đã gửi mã xác nhận về mail", ""))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("False", "Email bị sai", ""));
    }

    @PostMapping("check-key-number")
    public ResponseEntity<ResponseDTO> checkKeyNumber(@RequestParam("KeyNumber") int keyNumber) {
        String token = authen.getTokenForgotPassword(keyNumber);
        return token != null ? ResponseEntity.ok().body(new ResponseDTO("True", "Đã gửi mã xác nhận về mail", token))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO("False", "Thất bại", ""));
    }

    @PostMapping("reset-number")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestAttribute("keyNumber") int keyNumber,
            @RequestParam("Password") String password) {
        try {
            authen.resetPassWord(keyNumber, password);
            return ResponseEntity.ok().body(new ResponseDTO("True", "Cập nhật mậu khẩu thành công", ""));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO("False", "Cập nhật mật khẩu thất bại", ""));
        }
    }
}
