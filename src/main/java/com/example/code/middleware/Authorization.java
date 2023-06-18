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
    private static long EXPIRATION_TIME = 864000000;
    @Autowired
    DBUtil dbUtil;

        public String getToken(String email) {
            Date date = new Date();
            User user = dbUtil.getUserByEmail(email);
            
            Date expiration = new Date(date.getTime() + EXPIRATION_TIME);
            String token = Jwts.builder()
                    .setSubject(user.getUserID() +"")
                    .claim("Avatar",user.getAvatar())
                    .claim("Email", user.getEmail())
                    .claim("PASSWORD", user.getPASSWORD())
                    .claim("UserAddress", user.getUserAddress())
                    .claim("UserPhone", user.getUserPhone())
                    .claim("UserName", user.getUserName())
                    .setIssuedAt(date)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS256,secretKey)
                    .compact();
            return token;
        }

        public User parseToken(String token) {
            try {
                Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token) // Sửa đổi ở đây, sử dụng parseClaimsJws() thay vì parseClaimsJwt()
                .getBody();
                int id = Integer.parseInt(claims.getSubject()) ;
                String avatar = (String) claims.get("Avatar");
                String email = (String) claims.get("Email");
                String password = (String) claims.get("PASSWORD");
                String userAddress = (String) claims.get("UserAddress");
                String userPhone = (String) claims.get("UserPhone");
                String userName = (String) claims.get("UserName");
                User user = new User();
                user.setUserID(id);
                user.setAvatar(avatar);
                user.setEmail(email);
                user.setPASSWORD(password);
                user.setUserAddress(userAddress);
                user.setUserPhone(userPhone);
                user.setUserName(userName);
                return user;
            } catch (JwtException e) {
                return null;
            }

        }
        public boolean isValidUser(String token){
            try {
                Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token) // Sửa đổi ở đây, sử dụng parseClaimsJws() thay vì parseClaimsJwt()
                .getBody();
                return true;
            } catch (JwtException e) {
                return false;
            }
        }
        
}