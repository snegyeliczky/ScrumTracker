package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.Project;
import com.codecool.scrumtracker.model.Status;
import com.codecool.scrumtracker.model.credentials.ProjectCredentials;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;


import static org.assertj.core.api.Assertions.assertThat;

class ProjectServiceTest {

    private ProjectService projectService;

    @BeforeEach
    private void init(){
        projectService = new ProjectService();
    }

    @Test
    public void testCreateBaseStatus(){
        Util myUtil = mock(Util.class);
        when(myUtil.getUserFromContext()).thenReturn(new AppUser());
        ProjectCredentials bla = new ProjectCredentials("bla");
        projectService.createNewProject(bla);
    }

}