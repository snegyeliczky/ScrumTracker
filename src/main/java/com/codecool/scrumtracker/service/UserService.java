package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserService {

    @Autowired
    AppUserRepository appUserRepository;

    public Set<AppUser> searchUser(UserCredentials keyWord) {

        return appUserRepository.findByUsernameContaining(keyWord.getUsername());
    }
}
