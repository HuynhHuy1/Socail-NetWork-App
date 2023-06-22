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
        List<UserDTO> users = userDao.getUsersByName(userName);
        return users;
    }

    public void updateUser(String UserName, String Avatar, String UserAddress, String UserPhone, String Email,
            String PASSWORD, int id) {
        UserDTO user = new UserDTO(id, UserName, Avatar, UserAddress, UserPhone, Email, PASSWORD);
        userDao.updateUser(user);
    }

    public boolean changePassword(int userId, String oldPassWord, String passWord) {
        UserDTO user = userDao.getUserByID(userId);
        if (user.getPassword().equals(oldPassWord)) {
            userDao.updatePasswordByID(passWord, userId);
        }
        return false;
    }

}
