package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LoginRepositoryTest {
    @Test
    public void shouldGenerateTokenOnSuccess() throws IOException {
        UUID response = new LoginRepository().login(new UserCredentials("shr1", "foo"));
        assertNotNull(response);
        response = new LoginRepository().login(new UserCredentials("shr2", "bar"));
        assertNotNull(response);
    }

    @Test
    public void shouldNullOnFailure() throws IOException {
        UUID response = new LoginRepository().login(new UserCredentials("shr1", "foo1"));
        assertNull(response);

        response = new LoginRepository().login(new UserCredentials("shr3", "foo"));
        assertNull(response);
    }

    @Test
    public void shouldRememberTokenKeepUnique() throws IOException {
        LoginRepository loginRepository = new LoginRepository();
        UUID response1 = loginRepository.login(new UserCredentials("shr1", "foo"));
        UUID response2 = loginRepository.login(new UserCredentials("shr1", "foo"));
        assertEquals(response1, response2);
    }

    @Test
    public void shouldRespondUserCredentialsGivenToken() throws IOException {
        LoginRepository loginRepository = new LoginRepository();
        UUID token = loginRepository.login(new UserCredentials("shr1", "foo"));
        UserCredentials userCredentials = loginRepository.getUserByToken(token);
        assertEquals("shr1", userCredentials.getName());
    }

}