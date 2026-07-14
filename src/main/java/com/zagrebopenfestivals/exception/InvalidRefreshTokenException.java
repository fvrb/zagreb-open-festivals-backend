package com.zagrebopenfestivals.exception;

public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Refresh token je nevažeći ili je istekao");
    }
}
