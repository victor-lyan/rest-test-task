package com.example.resttesttask.config;

import com.example.resttesttask.service.UserService;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private UserService userService;

    public MyApplicationListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        // on authentication failure we update login tries count for this user
        String username = (String) event.getAuthentication().getPrincipal();
        userService.updateLoginTries(username);
    }
}
