package com.example.code.service;


import org.springframework.stereotype.Component;

import com.example.code.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class AuthorizationService {
    private static String secretKey = "MyKey";

    public String generateToken(UserDTO user) {
        return Jwts.builder()
                .setSubject(user.getId() + "")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public int parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return Integer.parseInt(claims.getSubject());
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
    public String generateTokenResetPassword(int keyNumber) {
        return Jwts.builder()
                .setSubject(keyNumber + "")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}