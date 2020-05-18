package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import com.codecool.scrumtracker.security.JwtTokenServices;
import com.codecool.scrumtracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final PasswordEncoder passwordEncoder;

    public AuthController( AppUserRepository users) {

        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @PostMapping("/registration")
    public void registration(@RequestBody UserCredentials newUser){
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        authService.registration(newUser);
    }

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody UserCredentials data, HttpServletResponse response) {
        return authService.signIn(data,response);
    }
}
