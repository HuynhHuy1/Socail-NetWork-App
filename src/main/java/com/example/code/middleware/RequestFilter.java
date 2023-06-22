package com.example.code.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.code.service.AuthorizationService;
import com.example.code.dao.UserDao;
import com.example.code.dto.ResponseDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilter implements HandlerInterceptor {
    @Autowired
    AuthorizationService authorization;
    @Autowired
    UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requString = request.getRequestURL().toString();
        if (requString.contains("api")) {
            try {
                String token = request.getHeader("Token");
                if (authorization.isValidUser(token)) {
                    int userID = authorization.parseToken(token);
                    request.setAttribute("userID", userID);
                    return true;
                } else {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(new ResponseDTO("False", "Loi xac thuc nguoi dung", "").toJsonString());
                    return false;
                }
            } catch (Exception e) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write(new ResponseDTO("False", "Loi xac thuc nguoi dung", "").toJsonString());
                return false;
            }
        } else if (requString.contains("reset-password")) {
            try {
                String token = request.getHeader("keyNumber");
                if (authorization.isValidUser(token)) {
                    int keyNumber = authorization.parseToken(token);
                    request.setAttribute("keyNumber", keyNumber);
                    return true;
                } else {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(new ResponseDTO("False", "Đổi mật khẩu thất bại", "").toJsonString());
                    return false;
                }
            } catch (Exception e) { 
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write(new ResponseDTO("False", "Đổi mật khẩu thất bại", "").toJsonString());
                return false;
            }
        }
        return true;
    }
}