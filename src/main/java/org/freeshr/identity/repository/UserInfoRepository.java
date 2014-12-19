package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

@Component
public class UserInfoRepository extends Repository {
    Map<String, UserInfo> users = new HashMap<>();

    public UserInfoRepository() {
        Properties properties = loadProperties();
        for (String user : properties.stringPropertyNames()) {
           users.put(user, new UserInfo(user, getRoles(), "603602" ));
        }
    }

    private HashSet<String> getRoles() {
        HashSet<String> strings = new HashSet<>();
        strings.add("mci.admin");
        strings.add("shr.user");
        return strings;
    }

    public UserInfo getUserInfo(String user) {
        return users.get(user);
    }
}
