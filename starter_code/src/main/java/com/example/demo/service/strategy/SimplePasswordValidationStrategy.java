package com.example.demo.service.strategy;

import org.springframework.stereotype.Component;

@Component
public class SimplePasswordValidationStrategy implements PasswordValidationStrategy {

    @Override
    public boolean isValid(String password, String confirmPassword) {
        return password != null
                && password.length() >= 7
                && password.equals(confirmPassword);
    }

}
