package com.example.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.dao.UserDao;
import com.example.code.dto.UserDTO;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<UserDTO> getUserByName(String userName) {
        return userDao.getUsersByName(userName);
    }

    public void updateUser(UserDTO userUpdate,int idUserData) {
        UserDTO userData = userDao.getUserByID(idUserData);
        if (userUpdate.getAddress() == null) {
            userUpdate.setAddress(userData.getAddress());
        }
        
        if (userUpdate.getName() == null) {
            userUpdate.setName(userData.getName());
        }
        
        if (userUpdate.getAvatar() == null) {
            userUpdate.setAvatar(userData.getAvatar());
        }
        
        if (userUpdate.getPhone() == null) {
            userUpdate.setPhone(userData.getPhone());
        }
        userUpdate.setId(idUserData);
        userDao.updateUser(userUpdate);
    }

    public boolean changePassword(int userId, String oldPassWord, String passWord) {
        UserDTO user = userDao.getUserByID(userId);
        if (user.getPassword().equals(oldPassWord)) {
            userDao.updatePasswordByID(passWord, userId);
            return false;
        }
        return true;
    }
}
