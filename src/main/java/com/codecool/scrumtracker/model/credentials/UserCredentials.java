package com.codecool.scrumtracker.model.credentials;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentials {

    private String username;

    private String password;

    private String email;


}
