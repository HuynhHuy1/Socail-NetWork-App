package com.example.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.code.dao.UserDao;
import com.example.code.dto.UserDTO;
import com.example.code.util.FileUitl;


@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public List<UserDTO> getUserByName(String userName) {
        List<UserDTO> listUser = userDao.getUsersByName(userName);
        listUser.forEach( (user) -> {
            if(user.getAvatar() != null || user.getAvatar() != ""){
                user.setAvatar(new FileUitl().readFile(user.getAvatar()));
            }
        }); 
        return listUser;
    }

    public void updateUser(UserDTO userUpdate,int idUserData,MultipartFile avatar) {
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
        if(avatar != null){
            String filePath = new FileUitl().addFileToStorage(avatar);
            String avatarBase64 = new FileUitl().readFile(filePath);
            userUpdate.setAvatar(filePath);
        }
        userUpdate.setId(idUserData);
        userDao.updateUser(userUpdate);
    }

    public UserDTO getUserbyId(int id){
        UserDTO userDto = userDao.getUserByID(id);
        String imageBase64 = new FileUitl().readFile(userDto.getAvatar());
        userDto.setAvatar(imageBase64);
        return userDto;
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
