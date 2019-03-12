package com.example.resttesttask.controller;

import com.example.resttesttask.dto.ResultDto;
import com.example.resttesttask.exception.MyValidationException;
import com.example.resttesttask.exception.UserAlreadyExistsException;
import com.example.resttesttask.model.User;
import com.example.resttesttask.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserController {
    
    private UserService userService;
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/check-username/{username}")
    public ResultDto checkUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        ResultDto resultDto = new ResultDto();
        if (user == null) {
            resultDto.setStatus(ResultDto.STATUS_SUCCESS);
            resultDto.setMessage("Username available");
        } else {
            resultDto.setStatus(ResultDto.STATUS_ERROR);
            resultDto.setMessage("Username is already in use");
        }
        
        return resultDto;
    }
    
    @PostMapping("/users/create-user")
    public User createUser(@Valid @RequestBody User user, BindingResult result, HttpServletRequest request) 
        throws Exception {
        if (result.hasErrors()) {
            throw new MyValidationException(result.getAllErrors().stream()
                .map(x -> x.getDefaultMessage()).collect(Collectors.joining(",")));
        }
        
        // check username
        User userInDb = userService.findUserByUsername(user.getUsername());
        if (userInDb != null) {
            throw new UserAlreadyExistsException("Username already in use");
        }
        
        // save new user
        String notEncryptedPassword = user.getPassword();
        userService.addUser(user);

        // authenticate user manually with not encrypted password
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            user.getUsername(),
            notEncryptedPassword
        );
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);
        
        return user;
    }
}
