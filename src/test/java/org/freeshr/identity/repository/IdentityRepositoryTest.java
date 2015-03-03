package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.*;

public class IdentityRepositoryTest {
    @Test
    public void shouldGenerateTokenOnSuccess() throws IOException {
        UUID response = new IdentityRepository().login(new UserCredentials("shr1", "foo"));
        assertNotNull(response);
        response = new IdentityRepository().login(new UserCredentials("shr2", "bar"));
        assertNotNull(response);
    }

    @Test
    public void shouldNullOnFailure() throws IOException {
        UUID response = new IdentityRepository().login(new UserCredentials("shr1", "foo1"));
        assertNull(response);

        response = new IdentityRepository().login(new UserCredentials("shr3", "foo"));
        assertNull(response);
    }

    @Test
    public void shouldRememberTokenKeepUnique() throws IOException {
        IdentityRepository identityRepository = new IdentityRepository();
        UUID response1 = identityRepository.login(new UserCredentials("shr1", "foo"));
        UUID response2 = identityRepository.login(new UserCredentials("shr1", "foo"));
        assertEquals(response1, response2);
    }

    @Test
    public void shouldRespondUserCredentialsGivenToken() throws IOException {
        IdentityRepository identityRepository = new IdentityRepository();
        UUID token = identityRepository.login(new UserCredentials("shr1", "foo"));
        UserCredentials userCredentials = identityRepository.getUserByToken(token.toString());
        assertEquals("shr1", userCredentials.getEmail());
    }

    @Test
    public void shouldSignInWithValidClientCredentials() throws Exception {
        IdentityRepository identityRepository = new IdentityRepository();
        String token = identityRepository.signin(new UserCredentials("12345", "xyz", "shr1", "foo"));
        assertNotNull(token);
    }

    @Test
    public void shouldNotSignInWithInvalidXAuthToken() throws Exception {
        IdentityRepository identityRepository = new IdentityRepository();
        String token = identityRepository.signin(new UserCredentials("12345", "invalid", "shr1", "foo"));
        assertNull(token);
    }

    @Test
    public void shouldNotSignInWithInvalidPassword() throws Exception {
        IdentityRepository identityRepository = new IdentityRepository();
        String token = identityRepository.signin(new UserCredentials("12345", "xyz", "shr1", "foobar"));
        assertNull(token);
    }

    @Test
    public void shouldGetTheUserDetailsUsingEmail() throws Exception {
        IdentityRepository identityRepository = new IdentityRepository();
        String email = "shr1";
        String token = identityRepository.signin(new UserCredentials("12345", "xyz", email, "foo"));
        assertNotNull(token);
        UserInfo userInfo = identityRepository.getUserInfo(email);

        assertNotNull(userInfo);
        assertEquals("12345", userInfo.getId());
        assertEquals(email,userInfo.getEmail());
        assertEquals(token, userInfo.getAccessToken());
    }
}