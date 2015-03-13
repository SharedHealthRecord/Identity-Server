package org.freeshr.identity.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private Object id;
    @JsonProperty("catchment")
    private List<String> catchments;

    public UserProfile() {
    }

    public UserProfile(String name, Object id, List<String> catchments) {
        this.name = name;
        this.id = id;
        this.catchments = catchments;
    }

    public String getName() {
        return name;
    }

    public Object getId() {
        return id;
    }

    public List<String> getCatchments() {
        return catchments;
    }
}
