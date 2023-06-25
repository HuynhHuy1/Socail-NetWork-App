package com.example.code.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.code.dao.UserDao;
import com.example.code.dto.UserDTO;
import com.example.code.exception.ExistException;
import com.example.code.exception.NotFoundException;
import com.example.code.exception.NullException;
import com.example.code.staticmessage.ErrorMessage;

@Service
public class AuthenticationService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private DataSource dataSource;

    public String login(UserDTO userLogin) {
        UserDTO userData = userDao.getUserByEmail(userLogin.getEmail());
        String passwordData = userData.getPassword();
        String passwordLogin = userLogin.getPassword();
        if (passwordLogin.equals(passwordData)) {
            return generateToken(userLogin);
        } else {
            throw new NotFoundException(ErrorMessage.EMAIL_NOT_EXISTS);
        }
    }

    public String signUp(UserDTO userSignUp) {
        UserDTO userData = userDao.getUserByEmail(userSignUp.getEmail());
        if (userData == null) {
            userDao.insertUser(userSignUp);
            UserDTO userLateInsert = userDao.getUserByEmail(userSignUp.getEmail());
            return generateToken(userLateInsert);
        } else {
            throw new ExistException(ErrorMessage.EMAIL_EXISTS);
        }
    }

    public void sendConfirmationEmail(String email) {
        int min = 100_000;
        int max = 999_999;
        Random random = new Random();
        int keyNumber = random.nextInt(max - min + 1) + min;
        String subject = " Mã thay đổi mật khẩu ";
        if (userDao.getUserByEmail(email) != null) {
            emailService.sendEmail(email, subject, keyNumber + "");
            userDao.insertPasswordReset(email, keyNumber);
        } else {
            throw new NotFoundException(ErrorMessage.EMAIL_NOT_EXISTS);
        }
    }

    public String generateTokenResetPassword(int keyNumber) {
        if (userDao.getEmailByKey(keyNumber) != null) {
            return authorizationService.generateTokenResetPassword(keyNumber);
        } else {
            throw new NotFoundException(ErrorMessage.NUMBERKEY_NOT_EXISTS);
        }
    }

    /**
     * @param keyNumber
     * @param password
     * @throws RuntimeException
     */
    public void resetPassword(int keyNumber, String password) throws RuntimeException {
        String email = userDao.getEmailByKey(keyNumber);
        SpringManagedTransaction sqlSessionFactory = new SpringManagedTransaction(dataSource);
        Connection sqlSession = null;
        try {
            if (userDao.getUserByEmail(email) == null) {
                throw new NullException(ErrorMessage.EMAIL_NOT_EXISTS);
            } else if (userDao.getEmailByKey(keyNumber) == null) {
                throw new NullException(ErrorMessage.NUMBERKEY_NOT_EXISTS);
            }
            sqlSession = sqlSessionFactory.getConnection();
            sqlSession.setAutoCommit(false);
            userDao.updatePasswordByEmail(email, password);
            userDao.deletePasswordReset(email, keyNumber);
            sqlSession.commit();
        } catch (Exception e) {
            if (sqlSession != null) {
                try {
                    sqlSession.rollback();
                    throw new RuntimeException(e);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            if (sqlSession != null) {
                try {
                    sqlSession.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String generateToken(UserDTO user) {
        return authorizationService.generateToken(user);
    }
}
