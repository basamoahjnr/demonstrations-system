package com.yasobafinibus.nnmtc.demonstration.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import static jakarta.transaction.Transactional.TxType.REQUIRED;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

@Transactional(SUPPORTS)
public abstract class AbstractRepository<E, P> implements Serializable {

 
	private static final long serialVersionUID = -942178885959526578L;
	private final Class<E> entityClass;

    public AbstractRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public static <E> Optional<E> findOrEmpty(final DaoRetriever<E> retriever) {
        try {
            return Optional.of(retriever.retrieve());
        } catch (NoResultException ex) {
            //log
        }
        return Optional.empty();
    }

    protected abstract EntityManager getEntityManager();

    @Transactional(REQUIRED)
    public void create(E entity) {
        getEntityManager().persist(entity);
    }

    @Transactional(REQUIRED)
    public E edit(E entity) {
        return getEntityManager().merge(entity);
    }

    @Transactional(REQUIRED)
    public void remove(E entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    @SuppressWarnings("unchecked")
    public P getIdentifier(E entity) {
        return (P) getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    public E find(P id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<E> findAll() {
        CriteriaQuery<E> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<E> findRange(int startPosition, int size) {
        return findRange(startPosition, size, null);
    }

    @SuppressWarnings("unchecked")
    public List<E> findRange(int startPosition, int size, String entityGraph) {
        CriteriaQuery<E> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(size);
        q.setFirstResult(startPosition);
        if (entityGraph != null) {
            q.setHint("jakarta.persistence.loadgraph", getEntityManager().getEntityGraph(entityGraph));
        }
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery<Long> criteriaQuery = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        Root<E> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(getEntityManager().getCriteriaBuilder().count(root));
        Query query = getEntityManager().createQuery(criteriaQuery);
        return ((Long) query.getSingleResult()).intValue();
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName) {
        return findOrEmpty(() -> getEntityManager().createNamedQuery(namedQueryName, entityClass).getSingleResult());
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return findSingleByNamedQuery(namedQueryName, null, parameters);
    }

    public Optional<E> findSingleByNamedQuery(String namedQueryName, String entityGraph, Map<String, Object> parameters) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        TypedQuery<E> query = getEntityManager().createNamedQuery(namedQueryName, entityClass);
        rawParameters.forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        if (entityGraph != null) {
            query.setHint("jakarta.persistence.loadgraph", getEntityManager().getEntityGraph(entityGraph));
        }
        return findOrEmpty(query::getSingleResult);
    }

    public List<E> findByNamedQuery(String namedQueryName) {
        return findByNamedQuery(namedQueryName, -1);
    }

    public List<E> findByNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return findByNamedQuery(namedQueryName, parameters, -1);
    }

    @SuppressWarnings("unchecked")
    public List<E> findByNamedQuery(String namedQueryName, int resultLimit) {
        return findByNamedQuery(namedQueryName, Collections.EMPTY_MAP, resultLimit);
    }

    @SuppressWarnings("unchecked")
    public List<E> findByNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = getEntityManager().createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        rawParameters.forEach(entry -> query.setParameter(entry.getKey(), entry.getValue()));
        return query.getResultList();
    }

    public CriteriaQuery<E> getQuery() {
        return getEntityManager().getCriteriaBuilder().createQuery(entityClass);
    }

    @FunctionalInterface
    public interface DaoRetriever<E> {
        E retrieve() throws NoResultException;
    }

}
