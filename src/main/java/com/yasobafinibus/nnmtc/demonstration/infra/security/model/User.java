package com.yasobafinibus.nnmtc.demonstration.infra.security.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Cacheable
@Table(name = "users", indexes = {
        @Index(name = "i_username", columnList = "username")
})
@NamedQueries({
        @NamedQuery(name = "User.findByUserLoginToken",
                query = "select u from User u join u.loginTokens l where " +
                        "l.tokenHash= :tokenHash and " +
                        "l.type= :tokenType and " +
                        "l.expiredDate > current_timestamp order by l.createdOn"),
        @NamedQuery(name = "User.findByUsername",
                query = "select u from User u join fetch u.roles where u.username= :username")
})
public class User extends AbstractAuditEntity implements Serializable {


    private static final long serialVersionUID = 3433162327572738634L;
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    List<LoginToken> loginTokens = new ArrayList<>();
    @Email
    @NotEmpty
    @Column(name = "username", nullable = false, updatable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = true)
    private String password;

    @NotEmpty
    @Enumerated(EnumType.ORDINAL)
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")})
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "roles", nullable = false)
    private Set<UserRole> roles;

    public User(String username, String password, UserRole... userRoles) {
        this.username = username;
        this.password = password;
        Set<UserRole> roles = Arrays.stream(userRoles).collect(Collectors.toSet());
        this.getRoles().addAll(roles);
    }


    public User(String username) {
        this.username = username;
    }


    public User() {

    }

    public Set<UserRole> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void addRoles(UserRole role) {
        getRoles().add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVersion(), getUsername(), getPassword());
    }

    public List<LoginToken> getLoginTokens() {
        return this.loginTokens;
    }

    public void setLoginTokens(List<LoginToken> loginTokens) {
        this.loginTokens = loginTokens;
    }

    public @Email @NotEmpty String getUsername() {
        return this.username;
    }

    public void setUsername(@Email @NotEmpty String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
