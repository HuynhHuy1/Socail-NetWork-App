package com.example.code.Service;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.code.DAO.UserDao;
import com.example.code.EmailSerivce.EmailService;
import com.example.code.Model.User;
import com.example.code.Util.DBUtil;
import com.example.code.middleware.Authorization;
@Service
public class Authentication {
    private final UserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    DBUtil dbUtil;
    @Autowired
    Authorization authorization;

    public Authentication(UserDao userDao) {
        this.userDao = userDao;
    }
    public String login(Map<String,String> map){
        String email = map.get("Email");
        String password = map.get("Password");
        String token = authorization.getToken(email);
        User user = dbUtil.getUserByEmail(email);
        if(user == null){
            return " Email khong ton tai";
        }
        else{
            String value = user.getPASSWORD();
            if(value.equals(password) ) return token;
            return "Sai password";
        }
        }


    public String singUp(Map<String, String> infoUser){
        User user = dbUtil.getUserByEmail(infoUser.get("Email"));
        if(user == null){
            user = setUser(infoUser);
            userDao.insertUser(user);
            return authorization.getToken(infoUser.get("Email"));
        }
        else{
            return "Email đã tồn tại";
        }
    }  
    public boolean forgetPassword(String email){
        User user = dbUtil.getUserByEmail(email);
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
    public List<User> getFriend(String token){
        User user = authorization.parseToken(token) ;
        int id = user.getUserID();
        return userDao.getFriend(id);
    }
    private User setUser(Map<String, String> infoUser){
        User user = new User();
        infoUser.forEach((key, value) -> {
            if (key.equals("Email")) {
                user.setEmail(value);
            }
            if (key.equals("Password")) {
                user.setPASSWORD(value);
            }
            if (key.equals("Avatar")) {
                user.setAvatar(value);
            }
            if (key.equals("UserPhone")) {
                user.setUserPhone(value);
            }
            if (key.equals("UserAddress")) {
                user.setUserAddress(value);
            }
            if (key.equals("UserName")) {
                user.setUserName(value);
            }
        });
        return user;
    }
}
