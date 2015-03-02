package org.freeshr.identity.service;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.repository.IdentityRepository;
import org.freeshr.identity.repository.UserInfoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class IdentityServiceTest {

    @Mock
    IdentityRepository identityRepository;

    @Mock
    UserInfoRepository userInfoRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldVerifyUserCredentials() {
        UserCredentials userCredentials = new UserCredentials("champak", "bhumia");
        UUID uuid = UUID.randomUUID();
        when(identityRepository.login(userCredentials)).thenReturn(uuid);
        IdentityService identityService = new IdentityService(identityRepository, userInfoRepository);
        assertEquals(uuid, identityService.login(userCredentials));
        verify(identityRepository).login(userCredentials);
    }

    @Test
    public void shouldGetUserInfoForValidToken() throws Exception {
        UserCredentials userCredentials = new UserCredentials("mogambo", "khushua");
        UserInfo userInfo = new UserInfo("mogambo", null, "123");
        UUID uuid = UUID.randomUUID();
        when(identityRepository.login(userCredentials)).thenReturn(uuid);
        when(identityRepository.getUserByToken(uuid)).thenReturn(userCredentials);
        when(userInfoRepository.getUserInfo("mogambo")).thenReturn(userInfo);

        IdentityService identityService = new IdentityService(identityRepository, userInfoRepository);
        assertEquals(identityService.userInfo(uuid), userInfo);

    }

    @Test
    public void shouldRespondNullForInvalidToken() throws Exception {
        UserCredentials userCredentials = new UserCredentials("mogambo", "khushua");
        UserInfo userInfo = new UserInfo("mogambo", null, "123");
        UUID uuid = UUID.randomUUID();
        when(identityRepository.login(userCredentials)).thenReturn(uuid);
        when(identityRepository.getUserByToken(uuid)).thenReturn(userCredentials);
        when(userInfoRepository.getUserInfo("mogambo")).thenReturn(userInfo);

        IdentityService identityService = new IdentityService(identityRepository, userInfoRepository);
        assertNull(identityService.userInfo(UUID.randomUUID()));
    }

    @Test
    public void shouldCallLoginRepositorySiginin() throws Exception {
        UserCredentials userCredentials = new UserCredentials("12345", "xyz", "mogambo", "khushua");
        new IdentityService(identityRepository, userInfoRepository).signin(userCredentials);
        verify(identityRepository, times(1)).signin(userCredentials);
    }
}