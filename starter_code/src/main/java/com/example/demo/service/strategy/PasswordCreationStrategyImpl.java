package com.example.demo.service.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordCreationStrategyImpl implements PasswordCreationStrategy {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public String generateEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
