package org.freeshr.identity.controller;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

@RestController
public class IdentityController {
    private IdentityService identityService;

    @Autowired
    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String login(@RequestBody UserCredentials userCredentials) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        UUID result = identityService.login(userCredentials);

        if (null == result) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        return result.toString();

    }

    @RequestMapping(value = "/userInfo/{token}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody UserInfo userInfo(@PathVariable UUID token) {
        UserInfo userInfo = identityService.userInfo(token);
        if (null == userInfo) {
            throw new BadCredentialsException("Invalid token");
        }
        return userInfo;
    }

}
