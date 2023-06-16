package com.example.code.Service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.code.DAO.UserDao;
import com.example.code.EmailSerivce.EmailService;
import com.example.code.Model.User;
@Service
public class Authentication {
    private final UserDao userDao;
    @Autowired
    private EmailService emailService;

    public Authentication(UserDao userDao) {
        this.userDao = userDao;
    }
    public Boolean login(Map<String,String> map){
        String email = map.get("Email");
        String password = map.get("Password");
        User user = getUserByMail(email);
        if(user == null){
            return false;
        }
        else{
            String value = user.getPASSWORD();
            return value.equals(password) ? true : false;
        }
        }
    public boolean singUp(Map<String, String> infoUser){
        User User = getUserByMail(infoUser.get("Email"));
        if(User == null){
            userDao.insertUser(User);
            return true;
        }
        else{
            return false;
        }
    }  
    public boolean forgetPassword(String email){
        User user = getUserByMail(email);
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
        String body = "Vui lòng nhấn vào link nào để thay đổi mật khẩu: http://localhost:8090/user/ResetPasswordForm";
        emailService.sendEmail(to, subject, body);
    }
    private User getUserByMail(String email){
        User User = userDao.getUserByEmail(email);
        return User;
    }
    public List<User> getAll(){
        List<User> User = userDao.getUserAll();
        return User;
    }
    public boolean resetPassword(String email, String passWord){
        User user = userDao.getUserByEmail(email);
        if(user == null){
            return false;
        }
        else{
            userDao.updatePassWord(passWord,email);
            return true;
        }
    }
    public List<User> getFriend(String email){
        int id = getIntByEmail(email);
        return userDao.getFriend(id);
        
    }
    private int getIntByEmail(String email){
        return userDao.getIDByEmail(email);
    }
}
