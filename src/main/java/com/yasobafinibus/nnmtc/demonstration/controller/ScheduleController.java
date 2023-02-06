package com.yasobafinibus.nnmtc.demonstration.controller;

import com.yasobafinibus.nnmtc.demonstration.domain.Schedule;
import com.yasobafinibus.nnmtc.demonstration.domain.Tutor;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.ScheduleRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.TutorRepository;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Named
@ViewScoped
public class ScheduleController extends AbstractController<Schedule> {


    @EJB
    private ScheduleRepository repository;
    @EJB
    private TutorRepository tutorRepository;


    public ScheduleController() {
        this.selected = new Schedule();
    }

    @Override
    public AbstractRepository<Schedule, Integer> getRepository() {
        return repository;
    }

    @Override
    public void create() {
        String name = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
        tutorRepository.findByTutorEmail(name).ifPresent(tutor -> selected.setTutor(tutor));
        super.create();
    }


    public List<Schedule> getTutorSchedules(String email) {
        List<Schedule> schedules = Collections.emptyList();
        if (email != null && tutorRepository.findByTutorEmail(email).isPresent()) {
            Tutor byTutorEmail = tutorRepository.findByTutorEmail(email).get();
            schedules = repository.getSchedules(byTutorEmail, LocalDateTime.now());
        }
        return schedules;
    }

    @Named
    @FacesConverter(managed = true, value = "scheduleConverter")
    public static class ScheduleConverter implements Converter<Schedule> {

        @EJB
        private ScheduleRepository repository;

        @Override
        public Schedule getAsObject(FacesContext context, UIComponent component, String value) {
            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Schedule value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
