package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserInfoRepositoryTest {
    @Test
    public void shouldRespondUserInfoForUser() throws Exception {
        UserInfo userInfo = new UserInfoRepository().getUserInfo("shr1");
        assertEquals("shr1", userInfo.getUser());
        assertEquals("603602", userInfo.getLocationCode());
        assertTrue(userInfo.getRoles().contains("ROLE_MCI_USER"));
        assertTrue(userInfo.getRoles().contains("ROLE_SHR_USER"));
        assertFalse(userInfo.getRoles().contains("shr.admin"));
    }
}