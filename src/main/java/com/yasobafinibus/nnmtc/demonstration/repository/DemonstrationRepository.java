package com.yasobafinibus.nnmtc.demonstration.repository;


import com.yasobafinibus.nnmtc.demonstration.domain.Demonstration;
import com.yasobafinibus.nnmtc.demonstration.domain.Demonstration_;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.Dependent;
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
public class DemonstrationRepository extends AbstractRepository<Demonstration, Integer> {

	

	private static final long serialVersionUID = 2125737560200398492L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;
    private CriteriaQuery<Demonstration> query;

    public DemonstrationRepository() {
        super(Demonstration.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public List<Demonstration> findAllByDemonstrationName(@NotEmpty String cohortName) {
        query = getQuery();
        Root<Demonstration> from = query.from(Demonstration.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Demonstration_.name)), parameterExpression));
        return em.createQuery(query).setParameter(parameterExpression, String.format("%%%s%%", cohortName.toUpperCase()))
                .getResultStream()
                .collect(Collectors.toUnmodifiableList());
    }


    public Optional<Demonstration> findByDemonstrationName(@NotEmpty String cohortName) {
        query = getQuery();
        Root<Demonstration> from = query.from(Demonstration.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Demonstration_.name)), parameterExpression));
        return em.createQuery(query)
                .setParameter(parameterExpression, String.format("%%%s%%", cohortName.toUpperCase()))
                .getResultStream()
                .findFirst();
    }

}
