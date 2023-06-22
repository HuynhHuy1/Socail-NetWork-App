package com.example.code.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.code.dao.UserDao;
import com.example.code.dto.AuthenResponseDTO;
import com.example.code.dto.PasswordResetDTO;
import com.example.code.dto.UserDTO;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthorizationService authorizationService;

    public AuthenResponseDTO login(UserDTO userLogin) {
        UserDTO userData = userDao.getUserByEmail(userLogin.getEmail());
        if (userData == null) {
            return new AuthenResponseDTO(false, "");
        } else {
            String token = generateToken(userLogin);
            String passwordData = userData.getPassword();
            String passwordLogin = userLogin.getPassword();
            return (passwordLogin.equals(passwordData)) ? new AuthenResponseDTO(true, token)
                    : new AuthenResponseDTO(false, "");
        }
    }
    public AuthenResponseDTO signUp(UserDTO userSignUp) {
        UserDTO userData = userDao.getUserByEmail(userSignUp.getEmail());
        if (userData == null) {
            userDao.insertUser(userSignUp);
            UserDTO userLateInsert = userDao.getUserByEmail(userSignUp.getEmail());
            String token = generateToken(userLateInsert);
            return new AuthenResponseDTO(true, token);
        } else {
            return new AuthenResponseDTO(false, "");
        }
    }

    public boolean isEmailExist(String Email){
        UserDTO user = userDao.getUserByEmail(Email);
        return user != null ? true : false ;
    } 

    public void sendConfirmationEmail(String email) {
        int min = 100_000;
        int max = 999_999;
        Random random = new Random();
        int keyNumber = random.nextInt(max - min + 1) + min;
        String subject = " Mã thay đổi mật khẩu ";        
        emailService.sendEmail(email, subject, keyNumber + "");
        userDao.insertPasswordReset(email, keyNumber);
    }

    public boolean isKeyNumberExist(int keyNumber){
        PasswordResetDTO passwordResetRequestDto = userDao.getPasswordResetRequest(keyNumber);
        return passwordResetRequestDto != null ? true : false ;
    }
    public String generateTokenResetPassword(int keyNumber) {
            String token = authorizationService.generateTokenResetPassword(keyNumber);
            return token;
    }
    public void resetPassWord(int keyNumber, String passWord) {
        String email = userDao.getEmailByKey(keyNumber);
        userDao.updatePasswordByMail(email, passWord);
        userDao.deletePasswordReset(email, keyNumber);
    }

    private String generateToken(UserDTO user) {
        return authorizationService.generateToken(user);
    }
}
