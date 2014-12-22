package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Component
public class LoginRepository extends PropertyReader {
    Map<String, String> users = new HashMap<>();
    Map<UUID, UserCredentials> sessions = new HashMap<>();
    Map<String, UUID> userTokens = new HashMap<>();

    public LoginRepository() {
        Properties properties = loadProperties();
        for (String user : properties.stringPropertyNames()) {
            users.put(user, properties.getProperty(user));
        }
    }

    public UUID login(UserCredentials userCredentials) {
        UUID sessionId = findSessionId(userCredentials);
        if(null != sessionId) return sessionId;
        return matchUserNameAndPassword(userCredentials) ? createSession(userCredentials) : null;
    }

    private UUID findSessionId(UserCredentials userCredentials) {
        return userTokens.get(userCredentials.getName());
    }

    private boolean matchUserNameAndPassword(UserCredentials userCredentials) {
        String name = userCredentials.getName();
        return users.containsKey(name) && users.get(name).equals(userCredentials.getPassword());
    }

    private UUID createSession(UserCredentials userCredentials) {
        UUID uuid = UUID.randomUUID();
        sessions.put(uuid, userCredentials);
        userTokens.put(userCredentials.getName(), uuid);
        return uuid;
    }

    public UserCredentials getUserByToken(UUID token) {
        return sessions.get(token);
    }
}
