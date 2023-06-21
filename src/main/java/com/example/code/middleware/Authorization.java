package com.example.code.middleware;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.code.Model.User;
import com.example.code.Util.DBUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class Authorization {
    private static String secretKey = "MyKey";
    @Autowired
    DBUtil dbUtil;
        public String generateToken(User user) {
            String token = Jwts.builder()
                    .setSubject(user.getUserID() +"")
                    .signWith(SignatureAlgorithm.HS256,secretKey)
                    .compact();
            return token;
        }

        public int parseToken(String token) {
            try {
                Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token) 
                .getBody();
                int id = Integer.parseInt(claims.getSubject()) ;
                return id;
            } catch (JwtException e) {
                return 0;
            }

        }
        public boolean isValidUser(String token){
            try {
                Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
                return true;
            } catch (JwtException e) {
                return false;
            }
        }

        public String generateTokenForgotPassword(int keyNumber){
            String token = Jwts.builder()
            .setSubject(keyNumber+"")
            .signWith(SignatureAlgorithm.HS256,secretKey)
            .compact();
            return token;
        }

}