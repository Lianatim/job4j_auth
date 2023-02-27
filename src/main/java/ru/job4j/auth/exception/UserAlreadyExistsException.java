package ru.job4j.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAlreadyExistsException.class.getSimpleName());

    public UserAlreadyExistsException(String message) {
        super(message);
        LOGGER.error(message);
    }
}
