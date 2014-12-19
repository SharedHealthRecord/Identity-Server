package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserRepositoryTest {
    @Test
    public void shouldGenerateTokenOnSuccess() throws IOException {
        UUID response = new UserRepository().verify(new UserCredentials("shr1", "foo"));
        assertNotNull(response);
        response = new UserRepository().verify(new UserCredentials("shr2", "bar"));
        assertNotNull(response);
    }

    @Test
    public void shouldNullOnFailure() throws IOException {
        UUID response = new UserRepository().verify(new UserCredentials("shr1", "foo1"));
        assertNull(response);

        response = new UserRepository().verify(new UserCredentials("shr3", "foo"));
        assertNull(response);
    }

    @Test
    public void shouldKeepToken() throws IOException {
        UserRepository userRepository = new UserRepository();
        UUID response1 = userRepository.verify(new UserCredentials("shr1", "foo"));
        UUID response2 = userRepository.verify(new UserCredentials("shr1", "foo"));
        assertEquals(response1, response2);
    }

}