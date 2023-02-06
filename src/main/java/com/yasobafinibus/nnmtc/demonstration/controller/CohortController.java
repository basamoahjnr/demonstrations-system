package com.yasobafinibus.nnmtc.demonstration.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.yasobafinibus.nnmtc.demonstration.domain.Cohort;
import com.yasobafinibus.nnmtc.demonstration.domain.Program;
import com.yasobafinibus.nnmtc.demonstration.domain.Student;
import com.yasobafinibus.nnmtc.demonstration.domain.StudentDto;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.CohortRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static com.yasobafinibus.nnmtc.demonstration.util.Utils.copyFile;


@Named
@ViewScoped
public class CohortController extends AbstractController<Cohort> {


    private static final long serialVersionUID = -491471143376898309L;

    @EJB
    private CohortRepository repository;
    private Program program = new Program();
    private UploadedFile file;
    private List<Cohort> cohorts;

    public CohortController(){this.selected = new Cohort();}

    @PostConstruct
    public void init() {
        this.cohorts = Collections.unmodifiableList(getRepository().findAll());
    }

    @SuppressWarnings("unchecked")
    public void upload() {

        try {

            List<StudentDto> dtos = new CsvToBeanBuilder(new InputStreamReader(file.getInputStream()))
                    .withIgnoreEmptyLine(true)
                    .withStrictQuotes(true)
                    .withType(StudentDto.class)
                    .build()
                    .parse();

            dtos.stream()
                    .map(dto -> new Student(dto.number, dto.surname, dto.otherName, dto.email, program))
                    .forEach(student -> getSelected().addStudent(student));

            Messages.addGlobalInfo(getFile().getFileName() + " is uploaded.");

            copyFile(file.getFileName(), file.getInputStream(), "/tmp/");
        } catch (IOException | IllegalStateException e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);//find what exception it is

            //handle if its any of csv exception
            if (rootCause.getClass() == CsvRequiredFieldEmptyException.class || rootCause.getClass() == ParseException.class) {
                Messages.addError("messages", ResourceBundle.getBundle("/Bundle").getString("CSVExceptionMessage"));
            } else {
                Messages.addError("messages", ResourceBundle.getBundle("/Bundle").getString("FileUploadErrorMessage"));
            }

            file = null;//reset file

            //TODO handle exception properly
            e.printStackTrace();
        }
    }


    /* method to complete cohort autocomplete search*/
    public List<Cohort> completeCohortName(String query) {
        List<Cohort> cohorts = repository.findAll() == null ? Collections.emptyList() : repository.findAll();
        return query.isEmpty() || query.isBlank() ? cohorts : repository.findAllByCohortName(query.toLowerCase());
    }


    /*notify of cohort selection*/
    public void onItemSelect(SelectEvent<Cohort> event) {
        Messages.addGlobalInfo("Cohort Selected");
    }

    @Override
    public void update() {
        super.update();
        setSelected(new Cohort());//reset selected demonstration
    }


    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public AbstractRepository<Cohort, Integer> getRepository() {
        return repository;
    }

    public void setCohorts(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }

    public List<Cohort> getCohorts() {
        return cohorts;
    }

    @Named
    @FacesConverter(managed = true, value = "cohortConverter")
    public static class CohortConverter implements Converter<Cohort> {

        @EJB
        private CohortRepository repository;

        @Override
        public Cohort getAsObject(FacesContext context, UIComponent component, String value) {
            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }


        @Override
        public String getAsString(FacesContext context, UIComponent component, Cohort value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
