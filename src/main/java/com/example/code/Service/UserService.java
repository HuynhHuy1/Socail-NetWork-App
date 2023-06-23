package com.example.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.code.dao.UserDao;
import com.example.code.dto.UserDTO;
import com.example.code.exception.IncorrectException;
import com.example.code.staticmessage.ErrorMessage;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<UserDTO> getUserByName(String userName) {
        return userDao.getUsersByName(userName);
    }

    public void updateUser(UserDTO userDto) {
        userDao.updateUser(userDto);
    }

    public void changePassword(int userId, String oldPassWord, String passWord) {
        UserDTO user = userDao.getUserByID(userId);
        if (user.getPassword().equals(oldPassWord)) {
            userDao.updatePasswordByID(passWord, userId);
        }
        else{
            throw new IncorrectException(ErrorMessage.INCORRECT_PASSWORD);
        }
    }
}
