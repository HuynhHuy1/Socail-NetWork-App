package com.example.code.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.code.dto.UserDTO;
import com.example.code.util.DBUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class AuthorizationService {
    private static String secretKey = "MyKey";
    @Autowired
    DBUtil dbUtil;

    public String generateToken(UserDTO user) {
        String token = Jwts.builder()
                .setSubject(user.getId() + "")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

    public int parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            int id = Integer.parseInt(claims.getSubject());
            return id;
        } catch (JwtException e) {
            return 0;
        }

    }

    public boolean isValidUser(String token) {
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

    public String generateTokenForgotPassword(int keyNumber) {
        String token = Jwts.builder()
                .setSubject(keyNumber + "")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return token;
    }

}