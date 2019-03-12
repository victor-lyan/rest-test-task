package com.example.resttesttask.config;

import com.example.resttesttask.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private RestAuthenticationEntryPoint entryPoint;

    public SecurityConfig(
        PasswordEncoder passwordEncoder,
        UserService userService,
        AuthenticationSuccessHandler successHandler,
        AuthenticationFailureHandler failureHandler, 
        RestAuthenticationEntryPoint entryPoint
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.entryPoint = entryPoint;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(entryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/check-username/*").permitAll()
            .antMatchers("/users/create-user").anonymous()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .successHandler(successHandler)
            .failureHandler(failureHandler)
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }
    
    public MyAuthenticationFilter authenticationFilter() throws Exception {
        MyAuthenticationFilter filter = new MyAuthenticationFilter(userService);
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
