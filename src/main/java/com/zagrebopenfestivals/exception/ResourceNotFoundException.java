package com.zagrebopenfestivals.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String resourceName, Long id) {
        return new ResourceNotFoundException(resourceName + " s ID-em " + id + " ne postoji");
    }
}
