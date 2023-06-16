package com.example.code.Controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.code.DTO.PostDTO;
import com.example.code.Service.HomeService;
@RestController
@RequestMapping("Home")
public class HomeController {
    @Autowired
    HomeService homeService;
    @PostMapping("GetPostFriend")
    List<PostDTO> GetPostFriend (@RequestBody Map<String,String> mapEmail){
        String email = mapEmail.get("Email");
        return homeService.getPost(email);
    }
}
