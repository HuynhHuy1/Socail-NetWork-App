package com.example.code.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.Model.User;
import com.example.code.Service.Authentication;
import com.example.code.middleware.Authorization;



@RestController
@RequestMapping("User")
public class UserController {
    @Autowired
    private Authentication authen;
    @Autowired
    private Authorization author;
    @PostMapping("Login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> map){
        String token = authen.login(map);
        return ResponseEntity.ok().body(token) ;
    }
    @PostMapping("SignUp")
    public ResponseEntity<?> signUp(@RequestBody Map<String,String> userInfo){
        String token = authen.singUp(userInfo);
        return ResponseEntity.ok().body(token);
    }
    @PostMapping("ForgetPassWord")
    public ResponseEntity<?> forgetPassword (@RequestBody Map<String,String> mapEmail){
        String email = mapEmail.get("Email");
        return authen.forgetPassword(email) ?   ResponseEntity.ok().body("{\"message\": \"Đã gửi mail xác nhận\"}")
                                             : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Mail không tồn tại\"}");
    }
    @PostMapping("ResetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email,@RequestParam("password") String password ){
        return authen.resetPassword(email, password) ? ResponseEntity.ok().body("{\"message\": \"Đã thay đổi mật khẩu\"}") :ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Mail không tồn tại\"}");
    }
    @GetMapping("GetFriend")
    public ResponseEntity<?> getFriend(@RequestHeader String token){
        if(author.isValidUser(token)){        
            List<User> listFriend = authen.getFriend(token);
            return ResponseEntity.ok().body(listFriend);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Khong truy cap duoc");
        }
    }
}