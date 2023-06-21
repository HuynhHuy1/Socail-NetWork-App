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

import com.example.code.Model.Response;
import com.example.code.Service.Authentication;

@RestController
@RequestMapping("User")
public class UserController {
    @Autowired
    private Authentication authen;

    @PostMapping("Login")
    public ResponseEntity<Response> login(@RequestBody Map<String, String> map) {
        String token = authen.login(map);
        if (token.equals("PasswordError") || token.equals("EmailError")) {
            return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                    new Response("Failed", "Đăng nhập không thành công", ""));
        } else {
            return ResponseEntity.ok().body(
                    new Response("Ok", "Đăng nhập thành công", token));
        }
    }

    @PostMapping("SignUp")
    public ResponseEntity<Response> signUp(@RequestBody Map<String, String> userInfo) {
        String token = authen.singUp(userInfo);
        return token.equals("EmailError") ? ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .body(new Response("Failed", "Email đã tồn tại", ""))
                : ResponseEntity.ok().body(new Response("Ok", "Đăng ký thành công", token));
    }

    @PostMapping("ForgotPassWord")
    public ResponseEntity<Response> forgetPassword(@RequestParam("email") String email) {
        return authen.sendKeyNumbe(email)
                ? ResponseEntity.ok().body(new Response("True", "Đã gửi mã xác nhận về mail", ""))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("False", "Email bị sai", ""));
    }

    @PostMapping("CheckKeyNumber")
    public ResponseEntity<Response> checkKeyNumber(@RequestParam("KeyNumber") int keyNumber) {
        String token = authen.getTokenForgotPassword(keyNumber);
        return token != null ? ResponseEntity.ok().body(new Response("True", "Đã gửi mã xác nhận về mail", token))
        : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("False", "Thất bại", ""));
     }

    @PostMapping("ResetPassword")
    public ResponseEntity<Response> resetPassword(@RequestAttribute("numberKey") int numberKey,@RequestParam("Password") String passWord) {
        try {
            authen.resetPassWord(numberKey,passWord);
            return ResponseEntity.ok().body(new Response("True","Cập nhật mậu khẩu thành công",""));
        } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("False", "Cập nhật mật khẩu thất bại",""));
        }
    }
}
