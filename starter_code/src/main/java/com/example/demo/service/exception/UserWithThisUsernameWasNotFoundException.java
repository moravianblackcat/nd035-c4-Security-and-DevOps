package com.example.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User with this username was not found.")
public class UserWithThisUsernameWasNotFoundException extends RuntimeException {
}
