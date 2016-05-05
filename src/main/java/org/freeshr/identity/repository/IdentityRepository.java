package org.freeshr.identity.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class IdentityRepository extends PropertyReader {
    private final int USER_SESSION_TIMEOUT = 600;

    private Map<String, String> userPasswords = new HashMap<>();
    private Map<String, UserCredentials> sessions = new PassiveExpiringMap<>(USER_SESSION_TIMEOUT, SECONDS);
    private Map<String, UserInfo> userTokens = new PassiveExpiringMap<>(USER_SESSION_TIMEOUT, SECONDS);
    private Map<String, String> clients = new HashMap<>();
    private Map<String, String> users = new HashMap<>();

    IdentityRepository() throws IOException {
        loadUserPasswords();
        loadClients();
        loadUserProfiles();
    }

    private void loadClients() {
        Properties properties;
        properties = loadProperties("clients.properties");
        for (String user : properties.stringPropertyNames()) {
            clients.put(user, properties.getProperty(user));
        }
    }

    private void loadUserProfiles() throws IOException {
        Properties properties;
        properties = loadProperties("userDetail.properties");
        for (String user : properties.stringPropertyNames()) {
            users.put(user, properties.getProperty(user));
        }
    }

    private void loadUserPasswords() {
        Properties properties = loadProperties("userPassword.properties");
        for (String user : properties.stringPropertyNames()) {
            userPasswords.put(user, properties.getProperty(user));
        }
    }

    public String signin(UserCredentials userCredentials) throws IOException {
        return checkUserNameAndPassword(userCredentials) && checkClientIdAndAuthToken(userCredentials) ? getOrCreateSession(userCredentials) : null;
    }

    public boolean checkClientIdAndAuthToken(UserCredentials userCredentials) {
        String clientId = userCredentials.getClientId();
        return clients.containsKey(clientId) && clients.get(clientId).equals(userCredentials.getAuthToken());
    }

    private String getOrCreateSession(UserCredentials userCredentials) throws IOException {
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
        return userPasswords.containsKey(name) && userPasswords.get(name).equals(userCredentials.getPassword());
    }

    private String createSession(UserCredentials userCredentials) throws IOException {
        String uuid = UUID.randomUUID().toString();
        sessions.put(uuid, userCredentials);
        String email = userCredentials.getEmail();
        UserInfo userInfo = new ObjectMapper().readValue(users.get(email), UserInfo.class);
        userInfo.setAccessToken(uuid);
        userTokens.put(email, userInfo);
        return uuid;
    }

    public UserCredentials getUserByToken(String token) {
        return sessions.get(token);
    }

    public UserInfo getUserInfo(String email) {
        return userTokens.get(email);
    }
}
