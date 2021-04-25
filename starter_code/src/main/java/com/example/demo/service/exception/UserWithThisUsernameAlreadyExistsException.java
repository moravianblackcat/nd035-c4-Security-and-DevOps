package com.example.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User with this username already exists.")
public class UserWithThisUsernameAlreadyExistsException extends RuntimeException {
}
