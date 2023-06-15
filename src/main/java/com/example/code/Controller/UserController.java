package com.example.code.Controller;

    import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.code.Service.Authentication;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private Authentication authen;
    @PostMapping("Login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> map){
        return authen.login(map) ? ResponseEntity.ok().body("Đăng nhập thành công") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Đăng nhập thất bại");
    }
    @PostMapping("SignUp")
    public ResponseEntity<?> signUp(@RequestBody Map<String,String> userInfo){
        if(authen.singUp(userInfo)){
            return ResponseEntity.ok().body("Đăng nhập thành công");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Gmail bị trùng");
    }

    
}