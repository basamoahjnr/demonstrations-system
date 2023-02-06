package com.yasobafinibus.nnmtc.demonstration.infra.security.repository;


import com.yasobafinibus.nnmtc.demonstration.infra.security.InvalidUsernameException;
import com.yasobafinibus.nnmtc.demonstration.infra.security.model.LoginToken;
import com.yasobafinibus.nnmtc.demonstration.infra.security.model.LoginToken.LoginTokenBuilder;
import com.yasobafinibus.nnmtc.demonstration.infra.security.model.User;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import org.omnifaces.utils.security.MessageDigests;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.LoginToken.TokenType;
import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;
import static java.util.UUID.randomUUID;

@Stateless
@Transactional(SUPPORTS)
public class UserRepository {

    private static final String MESSAGE_DIGEST_ALGORITHM = "SHA-256";


    @Inject
    @DefaultEntityManager
    EntityManager entityManager;

    public String generate(String email, String ipAddress, String description, TokenType tokenType) {
        LocalDateTime expiration = LocalDateTime.now().plusDays(14L);
        return generate(email, ipAddress, description, tokenType, expiration);
    }

    public String generate(String username, String ipAddress, String description, TokenType tokenType, LocalDateTime expiration) {
        String rawToken = randomUUID().toString();
        User user = findByUsername(username).orElseThrow(() -> new InvalidUsernameException(username));
        String tokeHash = new String(MessageDigests.digest(rawToken, MESSAGE_DIGEST_ALGORITHM));

        LoginToken build = new LoginTokenBuilder()
                .withTokenHash(tokeHash.getBytes(StandardCharsets.UTF_8))
                .withExpiredDate(expiration)
                .withDescription(description)
                .withType(tokenType)
                .withIpAddress(ipAddress)
                .withUser(user)
                .build();

        user.getLoginTokens().add(build);
        getEntityManager().persist(user);
        return rawToken;
    }

    @Transactional(REQUIRED)
    public void remove(String loginToken) {
        removeByTokenHash(new String(MessageDigests.digest(loginToken, MESSAGE_DIGEST_ALGORITHM)).getBytes(StandardCharsets.UTF_8));
    }

    @Transactional(REQUIRED)
    public void removeByTokenHash(byte[] tokenHash) {
        getEntityManager().createNamedQuery("LoginToken.remove")
                .setParameter("tokenHash", tokenHash)
                .executeUpdate();
    }

    public Optional<User> findByUsername(String username) {
        return getEntityManager()
                .createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getResultStream().findFirst();
    }


    public User findByLoginToken(String token, TokenType tokenType) {
        try {
            return getEntityManager().createNamedQuery("User.findByUserLoginToken", User.class)
                    .setParameter("tokenHash", new String(MessageDigests.digest(token, MESSAGE_DIGEST_ALGORITHM)).getBytes(StandardCharsets.UTF_8))
                    .setParameter("tokenType", tokenType)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e);
            return null;
        }
    }

    public List<User> findAll() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> q = cb.createQuery(User.class);
        return getEntityManager().createQuery(q).getResultList();
    }

    @Transactional(REQUIRED)
    public User save(User entity) {
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
            return entity;
        } else {
            return getEntityManager().merge(entity);
        }
    }

    @Transactional(REQUIRED)
    public User findById(Long id) {
        return getEntityManager().find(User.class, id);
    }


    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}
