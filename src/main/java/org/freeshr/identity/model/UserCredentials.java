package org.freeshr.identity.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCredentials {
    @JsonProperty("user")
    private String email;
    @JsonProperty("password")
    private String password;

    private String clientId;
    private String authToken;

    public UserCredentials() {
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials(String clientId, String authToken, String email, String password) {
        this.clientId = clientId;
        this.authToken = authToken;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCredentials that = (UserCredentials) o;

        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;

        return true;
    }
}
