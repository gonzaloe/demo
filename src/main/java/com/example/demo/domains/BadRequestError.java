package com.example.demo.domains;

public class BadRequestError {

    private final String message = "bad request, do not try to do it again";

    public String getMessage() {
        return message;
    }
}
