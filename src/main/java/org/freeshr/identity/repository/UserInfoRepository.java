package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

@Component
public class UserInfoRepository extends PropertyReader {
    Map<String, UserInfo> users = new HashMap<>();

    public UserInfoRepository() {
        Properties properties = loadProperties("userDetail.properties");
        for (String user : properties.stringPropertyNames()) {
            users.put(user, new UserInfo(user, getRoles(), "603602"));
        }
    }

    private HashSet<String> getRoles() {
        HashSet<String> strings = new HashSet<>();
        strings.add("ROLE_MCI_USER");
        strings.add("ROLE_SHR_USER");
        return strings;
    }

    public UserInfo getUserInfo(String user) {
        return users.get(user);
    }
}
