package com.example.bloghw3.user.exception;

public class UserDuplicationException extends RuntimeException {
    public UserDuplicationException(String message) {
        super(message);
    }
}
