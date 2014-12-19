package org.freeshr.identity.controller;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.service.UserCredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

@RestController
public class IdentityController {
    private UserCredentialsService userCredentialsService;

    @Autowired
    public IdentityController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody UserCredentials userCredentials) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();
        UUID result = userCredentialsService.verify(userCredentials);

        if (null == result) {
            throw new BadCredentialsException("Invalid Credentials");
        }
        return result.toString();

    }
//
//    @RequestMapping(value = "/userInfo/{token}", method = RequestMethod.GET, produces = "application/json")
//    public DeferredResult<ResponseObject> getUserCredential(@PathVariable String token) {
//        DeferredResult<ResponseObject> deferredResult = new DeferredResult<ResponseObject>();
//        String result = userCredentialsService.getUserCredential(token);
//        if (StringUtils.isEmpty(result)) {
//            deferredResult.setErrorResult(new BadCredentialsException("Invalid token"));
//        }
//        ResponseObject responseObject = new ResponseObject();
//        responseObject.setLogin(result);
//        responseObject.setToken(token);
//        deferredResult.setResult(responseObject);
//        return deferredResult;
//    }

}
