package org.freeshr.identity.service;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdentityService {
    private IdentityRepository identityRepository;

    @Autowired
    public IdentityService(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    public String signin(UserCredentials userCredentials) {
        return identityRepository.signin(userCredentials);
    }

    public UserInfo userDetail(UserCredentials credentials, String token) {
        if(identityRepository.checkClientIdAndAuthToken(credentials)) {
            UserCredentials userCredentialsOfToken = identityRepository.getUserByToken(token);
            return userCredentialsOfToken == null ? null : identityRepository.getUserInfo(userCredentialsOfToken.getEmail());
        }
        return null;
    }
}
