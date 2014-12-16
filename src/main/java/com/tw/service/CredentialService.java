package com.tw.service;


import com.tw.controller.UserCredential;
import com.tw.repository.UserCredentialRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CredentialService {


    public UserCredentialRepo userCredentialRepo;

    @Autowired
    public CredentialService(UserCredentialRepo userCredentialRepo) {
        this.userCredentialRepo = userCredentialRepo;
    }

    public String save(UserCredential userCredentil) {
        String token = new String();
        token = UUID.randomUUID().toString() + userCredentil.getUserName();
        userCredentialRepo.save(token, userCredentil);
        return token;

    }

    public String getUserCredential(String token) {

        return userCredentialRepo.getUserByToken(token);
    }
}
