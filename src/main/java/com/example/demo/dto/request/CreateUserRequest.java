package com.example.demo.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
