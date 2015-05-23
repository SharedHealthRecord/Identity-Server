package org.freeshr.identity.controller;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    private IdentityRepository identityRepository;

    protected void checkForValidUserRequest(HttpServletRequest request) {
        String client_id = request.getHeader("client_id");
        String authToken = request.getHeader("X-Auth-Token");
        UserCredentials userCredentials = new UserCredentials(client_id, authToken, null, null);
        if (!identityRepository.checkClientIdAndAuthToken(userCredentials)) {
            throw new BadCredentialsException("Not authenticated");
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorInfo notFound(ResourceNotFoundException exception) {
        return new ErrorInfo(HttpStatus.NOT_FOUND.value(), true, "Not found");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequest.class)
    public ErrorInfo badRequest(BadRequest exception) {
        return new ErrorInfo(HttpStatus.BAD_REQUEST.value(), true, exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorInfo badCredentials(BadCredentialsException exception) {
        return new ErrorInfo(HttpStatus.UNAUTHORIZED.value(), true, exception.getMessage());
    }


}
