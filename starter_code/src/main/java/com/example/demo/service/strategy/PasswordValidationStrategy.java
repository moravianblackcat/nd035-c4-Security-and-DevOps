package com.example.demo.service.strategy;

public interface PasswordValidationStrategy {

    boolean isValid(String password, String confirmPassword);

}
