package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Program;
import com.yasobafinibus.nnmtc.demonstration.domain.Program_;
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
public class ProgramRepository extends AbstractRepository<Program, Integer> {

 
	private static final long serialVersionUID = 2726797730154884469L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;
    private CriteriaQuery<Program> query;

    public ProgramRepository() {
        super(Program.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * find a tutor with programName
     **/
    //FIXME note that this will fail if a user is inserted directly into the users table
    public Optional<Program> findByProgramName(@NotEmpty String programName) {
        query = getQuery();
        Root<Program> from = query.from(Program.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Program_.name)), parameterExpression));
        return em.createQuery(query)
                .setParameter(parameterExpression, String.format("%%%s%%", programName.toUpperCase()))
                .getResultStream()
                .findFirst();
    }


    public List<Program> findAllProgramByName(@NotEmpty String query) {
        this.query = getQuery();
        Root<Program> from = this.query.from(Program.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        this.query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Program_.name)), parameterExpression));
        return em.createQuery(this.query)
                .setParameter(parameterExpression, String.format("%%%s%%", query.toUpperCase()))
                .getResultStream()
                .collect(Collectors.toUnmodifiableList());
    }

}
