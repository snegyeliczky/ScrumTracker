package com.codecool.scrumtracker.controller;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/search")
    public Set<AppUser> searchUser(@RequestBody String keyWord) {

        return userService.searchUser(keyWord);
    }
}
