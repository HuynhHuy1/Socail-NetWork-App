package com.example.code.staticmessage;

public class ErrorMessage {

        //Authencation Error -- 100X --
        public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
        public static final String EMAIL_NOT_EXISTS = "EMAIL_NOT_EXISTS";
        public static final String VALID_ERROR = "VALID_ERROR";
        public static final String NUMBERKEY_NOT_EXISTS = "NUMBERKEY_NOT_EXISTS";
        public static final String INCORRECT_PASSWORD = "INCORRECT_PASSWORD";
        public static final String LOGIN_FAILED = "LOGIN_FAILED";

        // DataAcess Error --200x-
        public static final String USER_NOT_EXISTS = "USER_NOT_EXISTS";
        public static final String FRIENDSHIP_EXISTS = "FRIENDSHIP_EXISTS";
        public static final String FRIENDSHIP_NOT_EXISTS = "FRIENDSHIP_NOT_EXISTS";


        // Post Error --300x --
        public static final String POST_NOT_EXIST = "POST_NOT_EXIST";
        public static final String POST_NOT_OF_USER = "POST_NOT_OF_USER";
        public static final String COMMENT_NOT_OF_USER = "COMMENT_NOT_OF_USER";
        public static final String COMMENT_NOT_EXIST = "COMMENT_NOT_EXIST";
        public static final String LIKE_DUPLICATE = "LIKE_DUPLICATE";
        public static final String LIKE_NOT_EXIST = "LIKE_NOT_EXIST";
}