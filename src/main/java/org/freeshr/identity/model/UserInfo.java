package org.freeshr.identity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class UserInfo {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("is_active")
    private int isActive;
    @JsonProperty("activated")
    private boolean activated;
    @JsonProperty("activated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date activatedAt;
    @JsonProperty("last_login")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date lastLogin;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updatedAt;
    @JsonProperty("deleted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date deletedAt;
    @JsonProperty("groups")
    private List<String> groups;
    @JsonProperty("profiles")
    private List<UserProfile> profiles;

    public UserInfo() {
    }

    public UserInfo(String id, String name, String email, String accessToken, List<String> groups, List<UserProfile> profiles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = 1;
        this.activated = true;
        this.activatedAt = new Date();
        this.lastLogin = new Date();
        this.groups = groups;
        this.accessToken = accessToken;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.deletedAt = new Date();
        this.profiles = profiles;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<UserProfile> getProfiles() {
        return profiles;
    }
}
