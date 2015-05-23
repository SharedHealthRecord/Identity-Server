package org.freeshr.identity.controller;

public class BadRequest extends RuntimeException {

    private String errorMessage;

    public BadRequest(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
