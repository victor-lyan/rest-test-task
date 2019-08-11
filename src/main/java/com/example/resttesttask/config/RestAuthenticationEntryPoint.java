package com.example.resttesttask.config;

import com.example.resttesttask.exception.ErrorDetails;
import com.example.resttesttask.service.UserService;
import com.example.resttesttask.util.LoginCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private UserService userService;

    public RestAuthenticationEntryPoint(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response, 
        AuthenticationException e
    ) throws IOException {
        if (LoginCache.loginTriesExpiration.get(request.getUserPrincipal().getName())) {
            ObjectMapper om = new ObjectMapper();
            ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Authentication error",
                "You've tried to login more than 10 times, please, try again in an hour",
                99
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(om.writeValueAsString(errorDetails));
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }
}
