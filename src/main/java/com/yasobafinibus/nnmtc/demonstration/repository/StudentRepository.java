package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Student;
import com.yasobafinibus.nnmtc.demonstration.domain.Student_;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class StudentRepository extends AbstractRepository<Student, Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6003997037610333584L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;
    private CriteriaQuery<Student> query;

    public StudentRepository() {
        super(Student.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Student> findStudentByNumber(String studentNumber) {
        query = getQuery();
        Root<Student> from = query.from(Student.class);
        ParameterExpression<String> parameterExpression = em.getCriteriaBuilder().parameter(String.class);
        query.where(em.getCriteriaBuilder().like(em.getCriteriaBuilder().upper(from.get(Student_.number)), parameterExpression));
        return em.createQuery(query)
                .setParameter(parameterExpression, String.format("%%%s%%", studentNumber.toUpperCase()))
                .getResultStream()
                .collect(Collectors.toUnmodifiableList());
    }
}
