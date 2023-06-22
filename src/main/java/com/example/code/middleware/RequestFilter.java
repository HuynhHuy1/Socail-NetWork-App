package com.example.code.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.code.model.Response;
import com.example.code.service.AuthorizationService;
import com.example.code.dao.UserDao;

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
        if (requString.contains("Api")) {
            try {
                String token = request.getHeader("Token");
                if (authorization.isValidUser(token)) {
                    int userID = authorization.parseToken(token);
                    request.setAttribute("userID", userID);
                    return true;
                } else {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(new Response("False", "Loi xac thuc nguoi dung", "").toJsonString());
                    return false;
                }
            } catch (Exception e) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write(new Response("False", "Loi xac thuc nguoi dung", "").toJsonString());
                return false;
            }
        } else if (requString.contains("ResetPassword")) {
            try {
                String token = request.getHeader("KeyNumber");
                if (authorization.isValidUser(token)) {
                    int numberKey = authorization.parseToken(token);
                    request.setAttribute("numberKey", numberKey);
                    return true;
                } else {
                    response.setStatus(403);
                    response.setContentType("application/json");
                    response.getWriter().write(new Response("False", "Đổi mật khẩu thất bại", "").toJsonString());
                    return false;
                }
            } catch (Exception e) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.getWriter().write(new Response("False", "Đổi mật khẩu thất bại", "").toJsonString());
                return false;
            }
        }
        return true;
    }
}