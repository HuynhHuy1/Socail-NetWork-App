package com.example.code.Controller;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
            if (token.equals("PasswordError") || token.equals("EmailError") ){
            return  ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body(
                    new Response("Failed", "Đăng nhập không thành công", ""));
            } 
            else{
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

    @PostMapping("ForgetPassWord")
    public ResponseEntity<Response> forgetPassword(@RequestBody Map<String, String> mapEmail) {
        String email = mapEmail.get("Email");
        return authen.forgetPassword(email) ? ResponseEntity.ok().body(new Response("Ok","Đã gửi email xác nhận", ""))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Failed", "Emial không tồn tại",""));
    }
    @PostMapping("ResetPassword")
    public ResponseEntity<Response> resetPassword(@RequestParam("email") String email,
            @RequestParam("password") String password) {
        return authen.resetPassword(email, password)
                ? ResponseEntity.ok().body(new Response("Ok","Yêu cầu thay đổi mật khẩu thành công", ""))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Failed", "Email bị sai", ""));
    }

    // @GetMapping("GetFriend")
    // public ResponseEntity<?> getFriend(@RequestHeader String token) {
    //     if (author.isValidUser(token)) {
    //         List<User> listFriend = authen.getFriend(token);
    //         return ResponseEntity.ok().body(listFriend);
    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Khong truy cap duoc");
    //     }
    // }
}