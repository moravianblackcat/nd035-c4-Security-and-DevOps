package com.example.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Item with this ID was not found.")
public class ItemWithThisIdWasNotFoundException extends RuntimeException {
}
