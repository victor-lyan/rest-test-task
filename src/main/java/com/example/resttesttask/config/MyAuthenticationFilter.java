package com.example.resttesttask.config;

import com.example.resttesttask.dto.LoginRequest;
import com.example.resttesttask.service.UserService;
import com.example.resttesttask.util.LoginCache;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private UserService userService;

    public MyAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        LoginRequest loginRequest = this.getLoginRequest(request);

        UsernamePasswordAuthenticationToken authRequest;
        
        // check user login tries
        if (!userService.checkLoginTries(loginRequest.getUsername())) {
            LoginCache.loginTriesExpiration.put(loginRequest.getUsername(), true);

            // if user is not permitted to login yet, need to make sure he can't login even with
            // right credentials
            authRequest = new UsernamePasswordAuthenticationToken(
                "",
                ""
            );
        } else {
            LoginCache.loginTriesExpiration.remove(loginRequest.getUsername());
            
            authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            );
        }

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {
        BufferedReader reader = null;
        LoginRequest loginRequest = null;
        try {
            reader = request.getReader();
            ObjectMapper om = new ObjectMapper();
            loginRequest = om.readValue(reader, LoginRequest.class);
        } catch (IOException ex) {
            Logger.getLogger(MyAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(MyAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (loginRequest == null) {
            loginRequest = new LoginRequest();
        }

        return loginRequest;
    }
}
