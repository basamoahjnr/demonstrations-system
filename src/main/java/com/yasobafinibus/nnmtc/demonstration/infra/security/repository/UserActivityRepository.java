package com.yasobafinibus.nnmtc.demonstration.infra.security.repository;


import com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity;
import com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventOutcome;
import com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventType;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;


@Stateless
public class UserActivityRepository implements Serializable {

    @Inject
    @DefaultEntityManager
    private EntityManager entityManager;

    public void log(UserActivity activity) {
        getEntityManager().persist(activity);

    }

    public void log(Class<?> ip,
                    @NotNull EventType eventType,
                    @NotNull EventOutcome eventOutcome,
                    @NotEmpty String username,
                    @NotEmpty String message) {
        log(UserActivity.UserActivityBuilder.anUserActivity()
                .withLogger(ip.getSimpleName())
                .withEventType(eventType)
                .withEventOutcome(eventOutcome)
                .withUserName(username)
                .withMessage(message)
                .build());
    }

    private EntityManager getEntityManager() {
        return this.entityManager;
    }
}
