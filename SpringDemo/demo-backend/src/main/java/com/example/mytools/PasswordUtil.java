package com.example.mytools;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    public static Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 64, 2, 65536, 10);

    public static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String Argon2HashPassword(String rawPasseord) {
        return argon2PasswordEncoder.encode(rawPasseord);
    }

    public static String BCryptHashPassword(String rawPasseord) {
        return bCryptPasswordEncoder.encode(rawPasseord);
    }

    public static Boolean Argon2ConfirmPassword(String rawPassword, String hashPassword) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 64, 2, 65536, 10);
        return encoder.matches(rawPassword, hashPassword);
    }

    public static Boolean BCryptConfirmPassword(String rawPassword, String hashPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, hashPassword);
    }

    public static void main(String[] args) {
        String rawPassword = "123456";
        System.out.println(Argon2HashPassword(rawPassword));
        System.out.println(BCryptHashPassword(rawPassword));
    }


}

