package com.tw.repository;

import com.tw.controller.UserCredential;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class UserCredentialRepo {


    public HashMap<String, String> userCredentials = new HashMap<String, String>();


    public void save(String token, UserCredential userCredentil) {
        if (userCredentials.get(token) == null)
            userCredentials.put(token, userCredentil.toString());

    }

    public String getUserByToken(String token) {
        if (userCredentials.containsKey(token)) {
            return userCredentials.get(token);
        }
        return null;
    }
}
