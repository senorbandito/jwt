package com.frontend.security.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Integer id) {
        super("Could not find plan: " + id);
    }
}