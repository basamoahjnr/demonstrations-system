package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Tutor;
import com.yasobafinibus.nnmtc.demonstration.domain.Tutor_;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class TutorRepository extends AbstractRepository<Tutor, Integer> {


    private static final long serialVersionUID = -273693590637627502L;

    @Inject
    @DefaultEntityManager
    private EntityManager em;

    public TutorRepository() {
        super(Tutor.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * find a tutor with email
     **/
    //FIXME note that this will fail if a user is inserted directly into the users table
    public Optional<Tutor> findByTutorEmail(@NotEmpty String email) {
        CriteriaQuery<Tutor> query = getQuery();
        Root<Tutor> from = query.from(Tutor.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(from.get(Tutor_.email), parameterExpression));
        return em.createQuery(query).setParameter(parameterExpression, String.format("%%%s%%", email)).getResultStream().findFirst();
    }


}
