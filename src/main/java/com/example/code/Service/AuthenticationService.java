package com.example.code.service;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.util.DBUtil;
import com.example.code.dao.UserDao;
import com.example.code.dto.ForgotPasswordDTO;
import com.example.code.dto.UserDTO;
@Service
public class AuthenticationService {
    private final UserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    DBUtil dbUtil;
    @Autowired
    AuthorizationService authorization;

    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }
    public String login(Map<String,String> map){
        String email = map.get("Email");
        String password = map.get("Password");
        UserDTO user = dbUtil.getUserByEmail(email);
        if(user == null){
            return "EmailError";
        }
        else{
            String value = user.getPassword();
            String token = authorization.generateToken(user);
            if(value.equals(password) ) return token;
            return "PasswordError";
        }
        }
    public String signUp(Map<String, String> infoUser){
        UserDTO user = dbUtil.getUserByEmail(infoUser.get("Email"));
        if(user == null){
            user = setUser(infoUser);
            userDao.insertUser(user);
            return authorization.generateToken(user);
        }
        else{
            return "EmailError";
        }
    }  
    public boolean forgetPassword(String email){
        UserDTO user = dbUtil.getUserByEmail(email);
        if(user == null){
            return false;
        }
        else{
            sendEmail(email);
            return true;
        }
    }
    private void sendEmail(String email){
        String to = email;
        String subject = "Xác nhận thay đổi mật khẩu";
        String body = "Vui lòng nhấn vào link nào để thay đổi mật khẩu: http://localhost:8090/user/ResetPasswordForm" ;
        emailService.sendEmail(to, subject, body);
    }
    public List<UserDTO> getAll(){
        List<UserDTO> User = userDao.getUserAll();
        return User;
    }
    public boolean sendKeyNumbe(String email){
        UserDTO user = userDao.getUserByEmail(email);
        int min = 100_000; // Giá trị tối thiểu (100000)
        int max = 999_999; // Giá trị tối đa (999999)

        Random random = new Random();
        int keyNumber = random.nextInt(max - min + 1) + min;
        String subject = " Mã thay đổi mật khẩu";
        if(user != null){
            userDao.generateKey(email, keyNumber);
            emailService.sendEmail(email,subject,keyNumber + "");
            return true;
        }
        return false;
    }
    public UserDTO setUser(Map<String, String> infoUser){
        UserDTO user = new UserDTO();
        infoUser.forEach((key, value) -> {
            if (key.equals("Email")) {
                user.setEmail(value);
            }
            if (key.equals("Password")) {
                user.setPassword(value);
            }
            if (key.equals("Avatar")) {
                user.setAvatar(value);
            }
            if (key.equals("UserPhone")) {
                user.setPhone(value);
            }
            if (key.equals("UserAddress")) {
                user.setAddress(value);
            }
            if (key.equals("UserName")) {
                user.setName(value);
            }
        });
        return user;
    }
    public String getTokenForgotPassword(int keyNumber) {
        ForgotPasswordDTO forgotPassword = userDao.getForgotPassword(keyNumber);
        if(forgotPassword != null){
            String token = authorization.generateTokenForgotPassword(keyNumber);
            return token;
        }
        else{
            return null;
        }
    }
    public void resetPassWord(int numberKey ,String passWord) {
        String email = userDao.getEmailByKey(numberKey);
        userDao.resetPassWord(email, passWord);
        userDao.deletePorgotPassword(email, numberKey);
    }
}
