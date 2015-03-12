package org.freeshr.identity.service;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.repository.IdentityRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class IdentityServiceTest {

    @Mock
    IdentityRepository identityRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldCallLoginRepositorySiginin() throws Exception {
        UserCredentials userCredentials = new UserCredentials("12345", "xyz", "mogambo", "khushua");
        new IdentityService(identityRepository).signin(userCredentials);
        verify(identityRepository, times(1)).signin(userCredentials);
    }

    @Test
    public void shouldAskIdentifyRepositoryForDetailsWhenValidToken() throws Exception {
        String email = "mogambo@gmail.com";
        UserCredentials requesterUserCredentials = new UserCredentials("123456", "xyzabc", null, null);
        UserCredentials userCredentials = new UserCredentials("12345", "xyz", email, "khushua");
        UUID uuid = UUID.randomUUID();
        when(identityRepository.getUserByToken(uuid.toString())).thenReturn(userCredentials);
        when(identityRepository.checkClientIdAndAuthToken(requesterUserCredentials)).thenReturn(true);

        IdentityService identityService = new IdentityService(identityRepository);
        identityService.userDetail(requesterUserCredentials, uuid.toString());
        verify(identityRepository, times(1)).checkClientIdAndAuthToken(requesterUserCredentials);
        verify(identityRepository, times(1)).getUserByToken(uuid.toString());
        verify(identityRepository, times(1)).getUserInfo(userCredentials.getEmail());
    }

    @Test
    public void shouldNotFetchUserDetailWhenRequesterCredentialsIsNotValid() throws Exception {
        UserCredentials requesterUserCredentials = new UserCredentials("123456", "xyz", null, null);
        when(identityRepository.checkClientIdAndAuthToken(requesterUserCredentials)).thenReturn(false);

        IdentityService identityService = new IdentityService(identityRepository);
        identityService.userDetail(requesterUserCredentials, UUID.randomUUID().toString());
        verify(identityRepository, times(0)).getUserInfo(anyString());
    }

    @Test
    public void shouldNotFetchUserDetailWhenTokenIsNotValid() throws Exception {
        String email = "mogambo@gmail.com";
        UserCredentials requesterUserCredentials = new UserCredentials("123456", "xyzabc", null, null);
        UserCredentials userCredentials = new UserCredentials("12345", "xyz", email, "khushua");
        UUID uuid = UUID.randomUUID();
        when(identityRepository.getUserByToken(uuid.toString())).thenReturn(null);
        when(identityRepository.checkClientIdAndAuthToken(requesterUserCredentials)).thenReturn(true);

        IdentityService identityService = new IdentityService(identityRepository);
        identityService.userDetail(requesterUserCredentials, uuid.toString());
        verify(identityRepository, times(0)).getUserInfo(userCredentials.getEmail());
    }
}