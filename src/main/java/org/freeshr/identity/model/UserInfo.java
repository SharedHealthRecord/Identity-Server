package org.freeshr.identity.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class UserInfo {
    @JsonProperty("user")
    private String name;
    @JsonProperty("roles")
    private HashSet<String> roles;
    @JsonProperty("locationCode")
    private String locationCode;

    public UserInfo() {
    }

    public UserInfo(String name, HashSet<String> roles, String locationCode) {
        this.name = name;
        this.roles = roles;
        this.locationCode = locationCode;
    }

    public String getName() {
        return name;
    }

    public HashSet<String> getRoles() {
        return roles;
    }

    public String getLocationCode() {
        return locationCode;
    }
}
