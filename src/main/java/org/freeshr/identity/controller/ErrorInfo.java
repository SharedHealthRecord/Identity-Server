package org.freeshr.identity.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ErrorInfo implements Serializable {

    @JsonProperty
    private boolean error;
    @JsonProperty
    private String message;
    @JsonProperty
    private int code;

    public ErrorInfo(int code, boolean error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }

}