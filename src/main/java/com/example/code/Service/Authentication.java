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
    // public boolean updatePassword(String email){
    //     List<User> listUser = getUserByMail(email);
    //     if(listUser.size() == 0){
    //         return false;
    //     }
    //     else{
    //         updatePassword(email)
    //     }
    //     return false;
    // }
    // private User userMapper(Map<String, String> infoUser){
    //     User user = new User();
    //     user.setUserName(infoUser.get("UserName"));
    //     user.setAvatar(infoUser.get("Avatar"));
    //     user.setUserAddress(infoUser.get("UserAddress"));
    //     user.setUserPhone(infoUser.get("UserPhone"));
    //     user.setPASSWORD(infoUser.get("Password"));
    //     user.setEmail(infoUser.get("Email"));
    //     return user;
    // }
    private User getUserByMail(String email){
        User User = userDao.getUserByEmail(email);
        return User;
    }
}
