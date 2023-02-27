package ru.job4j.auth.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceNotFoundException.class.getSimpleName());
    public ResourceNotFoundException(String message) {
        super(message);
        LOGGER.error(message);
    }
}
