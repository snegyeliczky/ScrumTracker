package com.codecool.scrumtracker.service;

import com.codecool.scrumtracker.model.AppUser;
import com.codecool.scrumtracker.model.credentials.UserCredentials;
import com.codecool.scrumtracker.repository.AppUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @MockBean
    AppUserRepository appUserRepository;

    @Autowired
    UserService userService;

    @Test
    public void testSearchUser() {

        Set<AppUser> testUsers = Stream.of(AppUser.builder()
                .username("test user")
                .build()).collect(Collectors.toSet());

        Mockito.when(appUserRepository.findByUsernameContaining(any()))
                .thenReturn(testUsers);

        assertThat(userService.searchUser(UserCredentials.builder().build())).isEqualTo(testUsers);
    }
}
