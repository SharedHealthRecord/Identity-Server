package com.tw.controller;

import com.tw.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StubController {
    private CredentialService credentialService;

    @Autowired
    public StubController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String get() {
        return "Hello";
    }

    @RequestMapping(value = "/user/userInfo/{token}", method = RequestMethod.GET, produces = "application/json")
    public String getUserCredential(@PathVariable String token) {
        String result = credentialService.getUserCredential(token);
        if (result == null) {
            throw new ResourceNotFoundException();
        }
        return result;
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, consumes = "application/json")
    public String create(@RequestBody UserCredential userCredentil) {
        return credentialService.save(userCredentil);
    }
}
