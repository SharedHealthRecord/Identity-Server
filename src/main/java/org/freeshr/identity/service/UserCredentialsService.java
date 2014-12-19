package org.freeshr.identity.service;


import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserCredentialsService {
    private HashMap<String, String> mapTokenToUserNames = new HashMap<String, String>();
    private HashMap<String, String> mapUserNameToTokens = new HashMap<String, String>();
    private UserRepository userRepository;

    @Autowired
    public UserCredentialsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public java.util.UUID verify(UserCredentials userCredentials) {
        return userRepository.verify(userCredentials);
    }

//    public String getUserCredential(String token) {
//        return userRepository.getUserCredentials(token);
//    }
}
