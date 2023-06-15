package com.example.code.Service;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.example.code.DAO.UserDao;
import com.example.code.Model.User;
@Service
public class Authentication {
    private final UserDao userDao;

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
            return true;
        }
    }
    private User getUserByMail(String email){
        User User = userDao.getUserByEmail(email);
        return User;
    }
}
