package com.example.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    String sendValidateRegisterEmail(String email, String sessionId);

    String sendValidateResetEmail(String email);

    String validateAndRegister(String username, String password, String email, String code, String id);

    boolean resetPassword(String password, String email);

    String validateCode(String email, String code);
}
