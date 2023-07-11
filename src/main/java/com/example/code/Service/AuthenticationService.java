package com.example.code.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.code.dao.UserDao;
import com.example.code.dto.ResponseDTO;
import com.example.code.dto.UserDTO;
import com.example.code.staticmessage.ErrorMessage;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthorizationService authorizationService;

    public ResponseDTO login(UserDTO userLogin) {
        UserDTO userData = userDao.getUserByEmail(userLogin.getEmail());
        String passwordLogin = userLogin.getPassword();
        String passwordData = null;
        if (userData != null) {
            passwordData = userData.getPassword();

            if (passwordLogin.equals(passwordData)) {
                String token = generateToken(userData);
                int id = userData.getId();
                Map<String, Object> mapSignUp = new HashMap();
                mapSignUp.put("token", token);
                mapSignUp.put("id", id);
                return new ResponseDTO("Success", "Đăng nhập thành công", mapSignUp);
            }

        }
        return new ResponseDTO("Failed", ErrorMessage.LOGIN_FAILED, null);
    }

    public ResponseDTO signUp(UserDTO userSignUp) {
        UserDTO userData = userDao.getUserByEmail(userSignUp.getEmail());
        if (userData == null) {
            userDao.insertUser(userSignUp);
            UserDTO userLateInsert = userDao.getUserByEmail(userSignUp.getEmail());
            String token = generateToken(userLateInsert);
            int userId = userLateInsert.getId();
            Map<String, Object> mapSignUp = new HashMap();
            mapSignUp.put("token", token);
            mapSignUp.put("id", userId);
            return new ResponseDTO("Success", "Đăng nhập thành công", mapSignUp);
        }
        return new ResponseDTO("Failed", ErrorMessage.EMAIL_EXISTS, null);
    }

    public boolean sendConfirmationEmail(String email) {
        int min = 100_0;
        int max = 999_9;
        Random random = new Random();
        int keyNumber = random.nextInt(max - min + 1) + min;
        String subject = " Mã thay đổi mật khẩu ";
        if (userDao.getUserByEmail(email) != null) {
            emailService.sendEmail(email, subject, keyNumber + "");
            userDao.insertPasswordReset(email, keyNumber);
            return true;
        } else {
            return false;
        }
    }

    public ResponseDTO generateTokenResetPassword(int keyNumber) {
        if (userDao.getEmailByKey(keyNumber) != null) {
            String token = authorizationService.generateTokenResetPassword(keyNumber);
            return new ResponseDTO("Success", " Lấy thành công token ", token);
        }
        return new ResponseDTO("Failed", ErrorMessage.NUMBERKEY_NOT_EXISTS, null);
    }

    @Transactional
    public void resetPassword(int keyNumber, String password) {
        String email = userDao.getEmailByKey(keyNumber);
        int updateRows = userDao.updatePasswordByEmail(email, password);
        if (updateRows == 0) {
            throw new RuntimeException();
        }
        int deleteRows = userDao.deletePasswordReset(email, keyNumber);
        if (deleteRows == 0) {
            throw new RuntimeException();
        }
    }

    private String generateToken(UserDTO user) {
        return authorizationService.generateToken(user);
    }
}
