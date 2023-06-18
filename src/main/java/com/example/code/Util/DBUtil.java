    package com.example.code.Util;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import com.example.code.DAO.UserDao;
    import com.example.code.Model.User;
import com.example.code.middleware.Authorization;

    @Component
    public class DBUtil {
        private UserDao userDao;

        @Autowired
        public DBUtil(UserDao userDao){
            this.userDao = userDao;
        }
        public User getUserByToken(String token){
            Authorization author = new Authorization();
            int id = author.parseToken(token).getUserID();
            return userDao.getUserByID(id);
        }
        public int getUserIDByEmail(String email){
            return userDao.getIDByEmail(email);
        }
        public User getUserByEmail(String email){
            return userDao.getUserByEmail(email);
        }
    }
