package com.example.code.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResetPwController {
    @GetMapping("user/ResetPasswordForm")
    public String resetPassword(){
        return "index";
    }
}
