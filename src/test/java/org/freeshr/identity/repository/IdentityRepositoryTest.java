package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.model.UserProfile;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class IdentityRepositoryTest {
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
        assertEquals(email, userInfo.getEmail());
        assertEquals(token, userInfo.getAccessToken());
    }

    @Test
    public void shouldGetTheUserGroupsAndProfiles() throws Exception {
        IdentityRepository identityRepository = new IdentityRepository();
        String email = "shr1";
        String token = identityRepository.signin(new UserCredentials("12345", "xyz", email, "foo"));
        assertNotNull(token);
        UserInfo userInfo = identityRepository.getUserInfo(email);

        assertNotNull(userInfo);
        assertEquals(asList("Facility Admin"), userInfo.getGroups());
        UserProfile profile = userInfo.getProfiles().get(0);
        assertEquals("admin", profile.getName());
        assertEquals(18548, profile.getId());
        assertEquals(asList("3026"), profile.getCatchments());
    }
}