package org.freeshr.identity.service;


import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.repository.LoginRepository;
import org.freeshr.identity.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdentityService {
    private LoginRepository loginRepository;
    private UserInfoRepository userInfoRepository;

    @Autowired
    public IdentityService(LoginRepository loginRepository, UserInfoRepository userInfoRepository) {

        this.loginRepository = loginRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public java.util.UUID login(UserCredentials userCredentials) {
        return loginRepository.login(userCredentials);
    }

    public UserInfo userInfo(UUID token) {
        UserCredentials userCredentials = loginRepository.getUserByToken(token);
        return userCredentials == null ? null : userInfoRepository.getUserInfo(userCredentials.getName());
    }

}
