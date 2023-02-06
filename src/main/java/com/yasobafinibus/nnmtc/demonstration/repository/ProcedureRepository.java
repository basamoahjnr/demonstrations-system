package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Procedure;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;


@Stateless
public class ProcedureRepository extends AbstractRepository<Procedure, Integer> {

 

	private static final long serialVersionUID = 8321894587354883579L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;

    public ProcedureRepository() {
        super(Procedure.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
