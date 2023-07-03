package com.example.bloghw3.jwtutil;

public class TokenValidException extends RuntimeException{
    public TokenValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
