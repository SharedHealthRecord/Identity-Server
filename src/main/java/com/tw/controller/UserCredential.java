package com.tw.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCredential {
    @JsonProperty("user")
    String userName;
    @JsonProperty("role")
    String userRole;
    @JsonProperty("password")
    String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "UserCredential{" +
                "userName='" + userName + '\'' +
                ", userRole='" + userRole + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
