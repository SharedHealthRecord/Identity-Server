package org.freeshr.identity.service;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.repository.LoginRepository;
import org.freeshr.identity.repository.UserInfoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IdentityServiceTest {

    @Mock
    LoginRepository loginRepository;

    @Mock
    UserInfoRepository userInfoRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldVerifyUserCredentials(){
        UserCredentials userCredentials = new UserCredentials("champak", "bhumia");
        UUID uuid = UUID.randomUUID();
        when(loginRepository.login(userCredentials)).thenReturn(uuid);
        IdentityService identityService = new IdentityService(loginRepository, userInfoRepository);
        assertEquals(uuid, identityService.login(userCredentials));
        verify(loginRepository).login(userCredentials);
    }

    @Test
    public void shouldGetUserInfoForValidToken() throws Exception {
        UserCredentials userCredentials = new UserCredentials("mogambo", "khushua");
        UserInfo userInfo = new UserInfo("mogambo", null, "123" );
        UUID uuid = UUID.randomUUID();
        when(loginRepository.login(userCredentials)).thenReturn(uuid);
        when(loginRepository.getUserByToken(uuid)).thenReturn(userCredentials);
        when(userInfoRepository.getUserInfo("mogambo")).thenReturn(userInfo);

        IdentityService identityService = new IdentityService(loginRepository, userInfoRepository);
        assertEquals(identityService.userInfo(uuid), userInfo);

    }
}