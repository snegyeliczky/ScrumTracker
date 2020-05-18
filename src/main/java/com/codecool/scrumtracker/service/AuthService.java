package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AuthService {

    @Autowired
    private AppUserRepository appUserRepository;

    public void registration(UserCredentials newUser) {
        AppUser appUser = AppUser.builder()
                .username(newUser.getUsername())
                .password(newUser.getPassword())
                .roles(Arrays.asList("ADMIN"))
                .build();
        appUserRepository.saveAndFlush(appUser);
    }
}
