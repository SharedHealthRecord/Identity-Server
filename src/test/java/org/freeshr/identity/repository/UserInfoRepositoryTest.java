package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserInfo;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserInfoRepositoryTest {
    @Test
    public void shouldRespondUserInfoForUser() throws Exception {
        UserInfo userInfo = new UserInfoRepository().getUserInfo("shr1");
        assertEquals("shr1", userInfo.getName());
        assertEquals("603602", userInfo.getLocationCode());
        assertTrue(userInfo.getRoles().contains("mci.admin"));
        assertTrue(userInfo.getRoles().contains("shr.user"));
        assertFalse(userInfo.getRoles().contains("shr.admin"));
    }
}