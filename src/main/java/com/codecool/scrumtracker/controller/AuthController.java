package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.credentials.UserCredentials;

import com.codecool.scrumtracker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final PasswordEncoder passwordEncoder;

    public AuthController() {

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

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletResponse response) {
        return authService.logout(response);
    }
}
