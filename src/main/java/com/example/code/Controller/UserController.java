package com.example.code.Controller;

    import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.code.EmailSerivce.EmailService;
import com.example.code.Service.Authentication;


@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private Authentication authen;
    @Autowired
    private EmailService emailService;
    @PostMapping("Login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> map){
        return authen.login(map) ? ResponseEntity.ok().body("{\"message\": \"Người đăng nhập thành công\"}") 
                                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Người đăng nhập không thành công\"}");
    }
    @PostMapping("SignUp")
    public ResponseEntity<?> signUp(@RequestBody Map<String,String> userInfo){
        if(authen.singUp(userInfo)){
            return ResponseEntity.ok().body("{\"message\": \"Người dùng đã được tạo thành công\"}");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Gmail bị trùng");
    }
    @PostMapping("ForgetPassWord")
    public ResponseEntity<?> forgetPassword (@RequestBody Map<String,String> mapEmail){
        String email = mapEmail.get("Email");
        return authen.forgetPassword(email) ?   ResponseEntity.ok().body("{\"message\": \"Đã thay đổi mật khẩu\"}")
                                             : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Mail không tồn tại\"}");
    }   
    @GetMapping("Email")
    public String SendEmail(){
        
        String to = "huyhn2.21it@vku.udn.vn";
        String subject = "Hello";
        String body = "Hello Huy";
        emailService.sendEmail(to, subject, body);
        return "thanh cong";
    }
}