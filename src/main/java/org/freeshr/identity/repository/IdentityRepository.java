package org.freeshr.identity.repository;

import org.freeshr.identity.model.UserCredentials;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Component
public class IdentityRepository extends PropertyReader {
    Map<String, String> users = new HashMap<>();
    Map<String, String> clients = new HashMap<>();
    Map<UUID, UserCredentials> sessions = new HashMap<>();
    Map<String, UUID> userTokens = new HashMap<>();

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

    public UUID login(UserCredentials userCredentials) {
        return matchUserNameAndPassword(userCredentials) ? getOrCreateSession(userCredentials) : null;
    }

    public UUID signin(UserCredentials userCredentials) {
        return matchUserNameAndPassword(userCredentials) && matchClientIdAndAuthToken(userCredentials) ? getOrCreateSession(userCredentials) : null;
    }

    private boolean matchClientIdAndAuthToken(UserCredentials userCredentials) {
        String clientId = userCredentials.getClientId();
        return clients.containsKey(clientId) && clients.get(clientId).equals(userCredentials.getAuthToken());
    }

    private UUID getOrCreateSession(UserCredentials userCredentials) {
        UUID sessionId = findSessionId(userCredentials);
        if (null != sessionId) return sessionId;
        return createSession(userCredentials);
    }

    private UUID findSessionId(UserCredentials userCredentials) {
        return userTokens.get(userCredentials.getEmail());
    }

    private boolean matchUserNameAndPassword(UserCredentials userCredentials) {
        String name = userCredentials.getEmail();
        return users.containsKey(name) && users.get(name).equals(userCredentials.getPassword());
    }

    private UUID createSession(UserCredentials userCredentials) {
        UUID uuid = UUID.randomUUID();
        sessions.put(uuid, userCredentials);
        userTokens.put(userCredentials.getEmail(), uuid);
        return uuid;
    }

    public UserCredentials getUserByToken(UUID token) {
        return sessions.get(token);
    }
}
