package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Component
public class IdentityRepository extends PropertyReader {
    Map<String, String> users = new HashMap<>();
    Map<String, String> clients = new HashMap<>();
    Map<String, UserCredentials> sessions = new HashMap<>();
    Map<String, UserInfo> userTokens = new HashMap<>();

    public IdentityRepository() {
        Properties properties = loadProperties("userDetail.properties");
        for (String user : properties.stringPropertyNames()) {
            users.put(user, properties.getProperty(user));
        }
        properties = loadProperties("clients.properties");
        for (String user : properties.stringPropertyNames()) {
            clients.put(user, properties.getProperty(user));
        }
    }

    @Deprecated
    public UUID login(UserCredentials userCredentials) {
        return checkUserNameAndPassword(userCredentials) ? UUID.fromString(getOrCreateSession(userCredentials)) : null;
    }

    public String signin(UserCredentials userCredentials) {
        return checkUserNameAndPassword(userCredentials) && checkClientIdAndAuthToken(userCredentials) ? getOrCreateSession(userCredentials) : null;
    }

    public boolean checkClientIdAndAuthToken(UserCredentials userCredentials) {
        String clientId = userCredentials.getClientId();
        return clients.containsKey(clientId) && clients.get(clientId).equals(userCredentials.getAuthToken());
    }

    private String getOrCreateSession(UserCredentials userCredentials) {
        String sessionId = findSessionId(userCredentials);
        if (null != sessionId) return sessionId;
        return createSession(userCredentials);
    }

    private String findSessionId(UserCredentials userCredentials) {
        UserInfo userInfo = userTokens.get(userCredentials.getEmail());
        return userInfo != null ? userInfo.getAccessToken() : null;
    }

    private boolean checkUserNameAndPassword(UserCredentials userCredentials) {
        String name = userCredentials.getEmail();
        return users.containsKey(name) && users.get(name).equals(userCredentials.getPassword());
    }

    private String createSession(UserCredentials userCredentials) {
        String uuid = UUID.randomUUID().toString();
        sessions.put(uuid, userCredentials);
        userTokens.put(userCredentials.getEmail(), new UserInfo(userCredentials.getClientId(), "Anonymous", userCredentials.getEmail(), uuid));
        return uuid;
    }

    public UserCredentials getUserByToken(String token) {
        return sessions.get(token);
    }

    public UserInfo getUserInfo(String email) {
        return userTokens.get(email);
    }
}
