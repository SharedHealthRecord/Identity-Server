package org.freeshr.identity.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.freeshr.identity.model.UserCredentials;
import org.freeshr.identity.model.UserInfo;
import org.freeshr.identity.model.UserProfile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import static java.util.Arrays.asList;

@Component
public class IdentityRepository extends PropertyReader {
    Map<String, String> userPasswords = new HashMap<>();
    Map<String, List<String>> userGroups = new HashMap<>();
    Map<String, String> clients = new HashMap<>();
    Map<String, UserCredentials> sessions = new HashMap<>();
    Map<String, UserInfo> userTokens = new HashMap<>();
    private Map<String, List<UserProfile>> userProfiles = new HashMap<>();

    public IdentityRepository() throws IOException {
        loadUserPasswords();
        loadUserGroups();
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

    private void loadUserGroups() {
        Properties properties;
        properties = loadProperties("userGroups.properties");
        for (String user : properties.stringPropertyNames()) {
            userGroups.put(user, asList(StringUtils.split(properties.getProperty(user), ",")));
        }
    }

    private void loadUserProfiles() throws IOException {
        Properties properties;
        properties = loadProperties("userProfile.properties");
        for (String user : properties.stringPropertyNames()) {
            userProfiles.put(user, asList(new ObjectMapper().readValue(properties.getProperty(user), UserProfile[].class)));
        }
    }

    private void loadUserPasswords() {
        Properties properties = loadProperties("userPassword.properties");
        for (String user : properties.stringPropertyNames()) {
            userPasswords.put(user, properties.getProperty(user));
        }
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
        return userPasswords.containsKey(name) && userPasswords.get(name).equals(userCredentials.getPassword());
    }

    private String createSession(UserCredentials userCredentials) {
        String uuid = UUID.randomUUID().toString();
        sessions.put(uuid, userCredentials);
        String email = userCredentials.getEmail();
        userTokens.put(email,
                new UserInfo(userCredentials.getClientId(), "Anonymous", email, uuid,
                        userGroups.get(email),
                        userProfiles.get(email)));
        return uuid;
    }

    public UserCredentials getUserByToken(String token) {
        return sessions.get(token);
    }

    public UserInfo getUserInfo(String email) {
        return userTokens.get(email);
    }
}
