package com.codecool.scrumtracker.util;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Util {


    @Autowired
    AppUserRepository appUserRepository;

    public AppUser getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        return appUserRepository.findByUsername(username).get();
    }

    public boolean projectAuthorization(Project project) {
        AppUser currentUser = getUserFromContext();
        if (currentUser.getId() == project.getAuthor().getId()) {
            return true;
        }
        for (AppUser user : project.getParticipants()) {
            if (user.getId() == currentUser.getId()) return true;
        }
        return false;
    }

    public boolean checkUserIsProjectOwner(Project project) {
        AppUser currentUser = getUserFromContext();
        return currentUser.getId() == project.getAuthor().getId();
    }
}
