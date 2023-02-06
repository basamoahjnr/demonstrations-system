package com.yasobafinibus.nnmtc.demonstration.infra.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import static java.util.Objects.isNull;
import java.util.Set;

public class UserInfo implements Serializable {

    private String name;
    private Set<String> roles;

    public UserInfo(String name) {
        this.name = name;
    }

    public UserInfo(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public UserInfo() {
    }

    public void addUserInfoRoles(String role) {
        if (isNull(roles)) {
            roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
