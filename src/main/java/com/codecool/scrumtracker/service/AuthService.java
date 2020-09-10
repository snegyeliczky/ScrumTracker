package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import com.codecool.scrumtracker.security.JwtTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }

    @Autowired
    private AppUserRepository appUserRepository;


    public void registration(UserCredentials newUser) {
        AppUser appUser = AppUser.builder()
                .username(newUser.getUsername())
                .password(newUser.getPassword())
                .email(newUser.getEmail())
                .roles(Arrays.asList("ADMIN"))
                .projectsCount(0)
                .participantCount(0)
                .finishedTaskCount(0)
                .tasksCount(0)
                .build();
        appUserRepository.saveAndFlush(appUser);
    }

    public ResponseEntity signIn(UserCredentials data, HttpServletResponse response) {
        try {
            String username = data.getUsername();
            // authenticationManager.authenticate calls loadUserByUsername in CustomUserDetailsService
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtTokenServices.createToken(username, roles);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            response.addCookie(cookie);

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("roles", roles);
            model.put("token", token);
            return ResponseEntity.ok(data);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    public ResponseEntity logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", "logout");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        return ResponseEntity.ok(cookie.getMaxAge());
    }
}
