package org.freeshr.identity.service;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserCredentialsServiceTest {

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldVerifyUserCredentials(){
        UserCredentials userCredentials = new UserCredentials("user", "password");
        UUID uuid = UUID.randomUUID();
        when(userRepository.verify(userCredentials)).thenReturn(uuid);
        UserCredentialsService userCredentialsService = new UserCredentialsService(userRepository);
        assertEquals(uuid, userCredentialsService.verify(userCredentials));
        verify(userRepository).verify(userCredentials);
    }

}