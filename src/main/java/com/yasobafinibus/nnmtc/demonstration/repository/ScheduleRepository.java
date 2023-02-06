package com.yasobafinibus.nnmtc.demonstration.repository;

import com.yasobafinibus.nnmtc.demonstration.domain.Schedule;
import com.yasobafinibus.nnmtc.demonstration.domain.Tutor;
import com.yasobafinibus.nnmtc.demonstration.producer.DefaultEntityManager;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class ScheduleRepository extends AbstractRepository<Schedule, Integer> {


	private static final long serialVersionUID = 425702689579285850L;
	@Inject
    @DefaultEntityManager
    private EntityManager em;

    public ScheduleRepository() {
        super(Schedule.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public List<Schedule> getSchedules(Tutor tutor, LocalDateTime date) {
        return em.createNamedQuery("Schedule.findByStartDateAndTutor", Schedule.class)
                .setParameter("id", tutor.getId())
                .setParameter("startDate", date)
                .getResultStream()
                .collect(Collectors.toUnmodifiableList());


    }

}
