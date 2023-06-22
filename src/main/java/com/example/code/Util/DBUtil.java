package com.example.code.util;

import org.springframework.stereotype.Component;

import com.example.code.service.AuthorizationService;
import com.example.code.dao.UserDao;
import com.example.code.dto.UserDTO;

@Component
public class DBUtil {
    private UserDao userDao;

    public DBUtil(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDTO getUserByToken(String token) {
        AuthorizationService author = new AuthorizationService();
        int id = author.parseToken(token);
        return userDao.getUserByID(id);
    }

    public int getUserIDByEmail(String email) {
        return userDao.getIDByEmail(email);
    }

    public UserDTO getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
}
