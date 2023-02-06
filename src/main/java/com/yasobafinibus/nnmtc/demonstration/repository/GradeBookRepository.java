package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.*;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static jakarta.transaction.Transactional.TxType.REQUIRED;

@Stateless
public class GradeBookRepository extends AbstractRepository<GradeBook, Integer> {

  
	private static final long serialVersionUID = -6428932126039562971L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;

    public GradeBookRepository() {
        super(GradeBook.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Optional<GradeBook> findGradeBook(GradeBook gradeBook) {
        CriteriaQuery<GradeBook> query = getQuery();
        Root<GradeBook> from = query.from(GradeBook.class);

        ParameterExpression<Student> student = em.getCriteriaBuilder().parameter(Student.class);
        ParameterExpression<Demonstration> demonstration = em.getCriteriaBuilder().parameter(Demonstration.class);
        ParameterExpression<Schedule> schedule = em.getCriteriaBuilder().parameter(Schedule.class);

        query.where(
                em.getCriteriaBuilder().and(
                        em.getCriteriaBuilder().equal(from.get(GradeBook_.student), student),
                        em.getCriteriaBuilder().equal(from.get(GradeBook_.demonstration), demonstration),
                        em.getCriteriaBuilder().equal(from.get(GradeBook_.schedule), schedule)
                ));

        return em.createQuery(query)
                .setParameter(student, gradeBook.getStudent())
                .setParameter(demonstration, gradeBook.getDemonstration())
                .setParameter(schedule, gradeBook.getSchedule())
                .getResultStream()
                .findFirst();
    }

//    public Optional<GradeBook> findGradeBook(GradeBook gradeBook) {
//        Optional<GradeBook> first = em.createNamedQuery("GradeBook.findGradeBook", GradeBook.class)
//                .setParameter("student", gradeBook.getStudent())
//                .setParameter("demonstration", gradeBook.getDemonstration())
//                .setParameter("schedule", gradeBook.getSchedule())
//                .getResultStream().findFirst();
//        return first;
//    }

    @Transactional(REQUIRED)
    public List<GradeDto> generateGrades(GradeBook gradeBook) {

        String query = "select " +
                "       ss.startdate as scheduleStartDate,\n" +
                "       s.number     as studentNumber,\n" +
                "       s.surname    as studentSurname,\n" +
                "       s.othernames as studentOthernames,\n" +
                "       s.email      as studentEmail,\n" +
                "       c.code       as cohortCode,\n" +
                "       d.code       as demonstrationCode,\n" +
                "       sum(go.mark) as totalScore\n" +
                "from gradebooks g\n" +
                "         inner join demonstrations d on d.id = g.demonstration_id\n" +
                "         inner join students s on s.id = g.student_id\n" +
                "         inner join schedules ss on ss.id = g.schedule_id\n" +
                "         inner join cohorts c on c.id = g.cohort_id\n" +
                "         inner join grade_outcomes go on g.id = go.gradebook_id\n" +
                "where g.cohort_id = ?\n" +
                "  and g.schedule_id = ?\n" +
                "  and g.demonstration_id = ?\n" +
                "group by ss.startdate, s.number, s.surname, s.othernames, s.email, c.code, d.code;";

        return Collections.unmodifiableList((List<GradeDto>) em.createNativeQuery(query, "GradeDtoMapping")
                .setParameter(1, gradeBook.getCohort().getId())
                .setParameter(2, gradeBook.getSchedule().getId())
                .setParameter(3, gradeBook.getDemonstration().getId())
                .getResultList());

    }
}
