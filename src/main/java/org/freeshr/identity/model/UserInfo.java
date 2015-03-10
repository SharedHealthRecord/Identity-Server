package org.freeshr.identity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.asList;

public class UserInfo {
    //TODO : remove user, locationCode and roles, not used in the new HRM IdP
    @JsonProperty("user")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private String user;
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("roles")
    private HashSet<String> roles;
    @JsonProperty("locationCode")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private String locationCode;

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("id")
    private String id;
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private String email;
    @JsonProperty("is_active")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
    private int isActive;
    @JsonProperty("activated")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
    private boolean activated;
    @JsonProperty("activated_at")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date activatedAt;
    @JsonProperty("last_login")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date lastLogin;
    @JsonProperty("access_token")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private String accessToken;
    @JsonProperty("created_at")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updatedAt;
    @JsonProperty("deleted_at")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private Date deletedAt;
    @JsonProperty("groups")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private HashSet<String> groups;
    @JsonProperty("profiles")
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    private ArrayList<Map> profiles;

    public UserInfo() {
    }

    public UserInfo(String user, String locationCode, HashSet<String> roles) {
        this.user = user;
        this.roles = roles;
        this.locationCode = locationCode;
    }

    public UserInfo(String id, String name, String email, String accessToken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = 1;
        this.activated = true;
        this.activatedAt = new Date();
        this.lastLogin = new Date();
        this.groups = getGroups();
        this.accessToken = accessToken;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.deletedAt = new Date();
        this.profiles = getProfiles();
    }

    @Deprecated
    public HashSet<String> getRoles() {
        return roles;
    }

    @Deprecated
    public String getLocationCode() {
        return locationCode;
    }

    @Deprecated
    public String getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    private HashSet<String> getGroups() {
        HashSet<String> strings = new HashSet<>();
        strings.add("Facility Admin");
        strings.add("MIS Admin");
        strings.add("Report viewer");
        strings.add("API Consumer");
        strings.add("MCI Admin");
        strings.add("MCI Approver");
        return strings;
    }

    private ArrayList<Map> getProfiles() {
        ArrayList<Map> profiles = new ArrayList<>();
        HashMap<String, Object> profile = new HashMap<>();
        profile.put("name", "facility");
        profile.put("id", "10000069");
        profile.put("catchment", asList("3026"));
        profiles.add(profile);

        HashMap<String, Object> profile1 = new HashMap<>();
        profile1.put("name", "admin");
        profile1.put("id", getId());
        profile1.put("catchment", asList("3026"));
        profiles.add(profile1);

        return profiles;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
