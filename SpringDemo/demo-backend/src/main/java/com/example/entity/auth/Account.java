package com.example.entity.auth;

import lombok.Data;

@Data
public class Account {
    Integer id;
    String username;
    String password;
    String email;
}
