package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Cohort;
import com.yasobafinibus.nnmtc.demonstration.domain.Cohort_;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class CohortRepository extends AbstractRepository<Cohort, Integer> {


	private static final long serialVersionUID = -1158846753845700855L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;
    private CriteriaQuery<Cohort> query;

    public CohortRepository() {
        super(Cohort.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public Optional<Cohort> findByCohortName(@NotEmpty String cohortName) {
        query = getQuery();
        Root<Cohort> from = query.from(Cohort.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Cohort_.name)), parameterExpression));
        return em.createQuery(query)
                .setParameter(parameterExpression, String.format("%%%s%%", cohortName.toUpperCase()))
                .getResultStream()
                .findFirst();
    }

    public List<Cohort> findAllByCohortName(@NotEmpty String cohortName) {
        query = getQuery();
        Root<Cohort> from = query.from(Cohort.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Cohort_.name)), parameterExpression));
        return em.createQuery(query).setParameter(parameterExpression, String.format("%%%s%%", cohortName.toUpperCase()))
                .getResultStream()
                .collect(Collectors.toUnmodifiableList());
    }
}
