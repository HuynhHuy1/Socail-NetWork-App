package com.example.code.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.code.filter.RequestFilter;

@Configuration
public class RequestFilterConfig implements WebMvcConfigurer {
    @Autowired
    private RequestFilter requestFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestFilter);
    }
}