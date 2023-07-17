package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoBackendApplicationTests {

    @Autowired
    PasswordEncoder encoder;

    @Test
    void contextLoads() {
        System.out.println(encoder.encode("123456"));
    }

}
