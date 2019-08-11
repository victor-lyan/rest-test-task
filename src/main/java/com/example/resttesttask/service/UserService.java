package com.example.resttesttask.service;

import com.example.resttesttask.model.Role;
import com.example.resttesttask.model.User;
import com.example.resttesttask.repository.UserRepository;
import com.example.resttesttask.util.LoginCache;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username and/or password");
        }

        return user;
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        user.setLoginTries(0);
        userRepository.save(user);
    }

    public void dropLoginTries(User user) {
        user.setLoginTries(0);
        user.setLastLoginTryTime(null);
        userRepository.save(user);
        LoginCache.loginTriesExpiration.remove(user.getUsername());
    }

    public void updateLoginTries(String username) {
        if (LoginCache.loginTriesExpiration.get(username))
            return;
        
        User user = userRepository.findUserByUsername(username);
        if (user != null) {
            int newLoginTries = user.getLoginTries() + 1;
            LocalDateTime newLastLoginTryTime = LocalDateTime.now();
            user.setLoginTries(newLoginTries);
            user.setLastLoginTryTime(newLastLoginTryTime);
            userRepository.save(user);
        }
    }

    public boolean checkLoginTries(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return true;
        }

        // check number of tries
        if (user.getLoginTries() < 10) {
            return true;
        }

        // check time
        LocalDateTime lastLoginTryTime = user.getLastLoginTryTime();
        LocalDateTime currentTime = LocalDateTime.now();
        long diffInSeconds = Duration.between(lastLoginTryTime, currentTime).toMillis() / 1000;
        
        return diffInSeconds >= 3600;
    }
}
