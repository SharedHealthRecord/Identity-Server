package com.tw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Token not Exist")
public class ResourceNotFoundException extends RuntimeException {

}