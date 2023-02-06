package com.yasobafinibus.nnmtc.demonstration.infra.security.model;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import static jakarta.persistence.EnumType.STRING;


@Entity
@Cacheable
@Table(name = "login_tokens")
@NamedQuery(name = "LoginToken.remove", query = "delete from LoginToken lt  where lt.tokenHash=:tokenHash")
@NamedQuery(name = "LoginToken.removeExpired", query = "delete from LoginToken lt where lt.expiredDate < current_timestamp")
public class LoginToken extends AbstractAuditEntity implements Serializable {

    private static final long serialVersionUID = -1271518840679154154L;
    @Column(unique = true, name = "token_hash", length = 800)
    private byte[] tokenHash;
    @Column(name = "expired_on")
    private LocalDateTime expiredDate;
    @Column(length = 45, name = "ip_address")
    private String ipAddress;
    @Column(name = "description")
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "ID")
    private User user;
    @Column(name = "token_type")
    @Enumerated(STRING)
    private TokenType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginToken that = (LoginToken) o;

        if (!getId().equals(that.getId())) return false;
        return Arrays.equals(this.getTokenHash(), that.getTokenHash());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + Arrays.hashCode(getTokenHash());
        return result;
    }

    public byte[] getTokenHash() {
        return this.tokenHash;
    }

    public void setTokenHash(byte[] tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getExpiredDate() {
        return this.expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TokenType getType() {
        return this.type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public enum TokenType {
        REMEMBER_ME,
        API,
        RESET_PASSWORD
    }

    public static final class LoginTokenBuilder {
        private byte[] tokenHash;
        private LocalDateTime expiredDate;
        private String ipAddress;
        private String description;
        private User user;
        private TokenType type;

        public LoginTokenBuilder() {
        }

        public static LoginTokenBuilder aLoginToken() {
            return new LoginTokenBuilder();
        }

        public LoginTokenBuilder withTokenHash(byte[] tokenHash) {
            this.tokenHash = tokenHash;
            return this;
        }

        public LoginTokenBuilder withExpiredDate(LocalDateTime expiredDate) {
            this.expiredDate = expiredDate;
            return this;
        }

        public LoginTokenBuilder withIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public LoginTokenBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public LoginTokenBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public LoginTokenBuilder withType(TokenType type) {
            this.type = type;
            return this;
        }

        public LoginToken build() {
            LoginToken loginToken = new LoginToken();
            loginToken.setTokenHash(tokenHash);
            loginToken.setExpiredDate(expiredDate);
            loginToken.setIpAddress(ipAddress);
            loginToken.setDescription(description);
            loginToken.setUser(user);
            loginToken.setType(type);
            return loginToken;
        }
    }
}
