package com.yasobafinibus.nnmtc.demonstration.controller;

import com.yasobafinibus.nnmtc.demonstration.domain.Program;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.ProgramRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;

import java.util.Collections;
import java.util.List;


@Named
@RequestScoped
public class ProgramController extends AbstractController<Program> {


    @EJB
    private ProgramRepository repository;
    private List<Program> programs;

    @PostConstruct
    public void init() {
        programs = repository.findAll();
    }

    public ProgramController() {
        this.selected = new Program();
    }


    /* method to autocomplete program names*/
    public List<Program> completeProgramName(String query) {
        List<Program> programs = repository.findAll() == null ? Collections.emptyList() : repository.findAll();
        return query.isEmpty() || query.isBlank() ? programs : repository.findAllProgramByName(query.toLowerCase());
    }

    /* method to notify of program selection*/
    public void onItemSelect(SelectEvent<Program> event) {
        Program cohortName = event.getObject();
        Messages.addGlobalInfo("Cohort Selected");
    }

    @Override
    public AbstractRepository<Program, Integer> getRepository() {
        return repository;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public List<Program> getPrograms() {
        return programs;
    }


    @Named
    @FacesConverter(managed = true, value = "programConverter")
    public static class ProgramConverter implements Converter<Program> {

        private final ProgramRepository repository;

        @Inject
        public ProgramConverter(ProgramRepository repository) {
            this.repository = repository;
        }

        @Override
        public Program getAsObject(FacesContext context, UIComponent component, String value) {
            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }


        @Override
        public String getAsString(FacesContext context, UIComponent component, Program value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
