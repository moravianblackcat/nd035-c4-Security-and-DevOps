package com.example.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "You cannot submit an empty cart.")
public class YouCannotSubmitAnEmptyCart extends RuntimeException {
}
