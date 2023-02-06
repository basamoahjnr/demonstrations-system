package com.yasobafinibus.nnmtc.demonstration.producer;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Producer for injectable EntityManager
 */
@RequestScoped
public class EntityManagerProducer {


    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @Produces
    @DefaultEntityManager
    public EntityManager getEntityManager() {
        return em;
    }

}
