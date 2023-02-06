package com.yasobafinibus.nnmtc.demonstration.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.yasobafinibus.nnmtc.demonstration.domain.Demonstration;
import com.yasobafinibus.nnmtc.demonstration.domain.Procedure;
import com.yasobafinibus.nnmtc.demonstration.domain.ProcedureDto;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.DemonstrationRepository;
import com.yasobafinibus.nnmtc.demonstration.util.Utils;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.omnifaces.util.Messages;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.yasobafinibus.nnmtc.demonstration.util.Utils.copyFile;

@Named
@ViewScoped
public class DemonstrationController extends AbstractController<Demonstration> {

    @EJB
    private DemonstrationRepository repository;
    private Procedure procedure = new Procedure();
    private UploadedFile file;
    private List<Demonstration> demonstrations;


    public DemonstrationController() {
        this.selected = new Demonstration();
    }

    @PostConstruct
    public void init() {
        demonstrations = Collections.unmodifiableList(getRepository().findAll());
    }


    /* method to complete demonstration autocomplete search*/
    public List<Demonstration> completeDemonstrationName(String query) {
        return query.isEmpty() || query.isBlank() ? repository.findAll() : repository.findAllByDemonstrationName(query.toLowerCase());
    }


    /*notify of demonstration selection*/
    public void onItemSelect(SelectEvent<Demonstration> event) {
        Messages.addGlobalInfo("Demonstration Selected");
    }

    @SuppressWarnings("unchecked")
    public void upload() {
        try {

            List<ProcedureDto> dtos = new CsvToBeanBuilder(new InputStreamReader(file.getInputStream()))
                    .withIgnoreEmptyLine(true)
                    .withStrictQuotes(true)
                    .withType(ProcedureDto.class)
                    .build()
                    .parse();

            //create procedure(s) from csv
            dtos.forEach(dto -> getSelected().addProcedure(new Procedure(dto.position, dto.name)));//create procedures from dtos

            Messages.addGlobalInfo(file.getFileName() + " is uploaded.");

            copyFile(file.getFileName(), file.getInputStream(), Utils.TEMP_FOLDER);//copy file to temporary folder
        } catch (IOException | IllegalStateException exception) {

            Throwable rootCause = ExceptionUtils.getRootCause(exception);//find what exception it is

            //handle if its any of csv exception
            if (rootCause.getClass() == CsvRequiredFieldEmptyException.class || rootCause.getClass() == ParseException.class) {
                Messages.addGlobalError(ResourceBundle.getBundle("/Bundle").getString("CSVExceptionMessage"));
            } else {
                Messages.addGlobalError(ResourceBundle.getBundle("/Bundle").getString("FileUploadErrorMessage"));
            }

            setSelected(new Demonstration()); //reset selected
            //reset selected
            file = null;
            //TODO handle exception properly


        }
    }

    //add new procedure to demonstration
    public void addProcedure() {
        if (getProcedure().getName().length() > 0 && getProcedure().getPosition() > 0) {
            for (Procedure p : getSelected().getProcedures()) {
                if (Objects.equals(p.getPosition(), getProcedure().getPosition())) {
                    p.setName(getProcedure().getName());
                }
            }
            if (!getSelected().
                    getProcedures()
                    .stream()
                    .map(Procedure::getPosition).collect(Collectors.toList())
                    .contains(getProcedure().getPosition())) {
                getSelected().addProcedure(getProcedure());
            }
            Messages.addGlobalInfo("new procedure added to demonstration");
        } else {
            Messages.addGlobalError("invalid procedure, please try again");
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            Messages.addGlobalInfo("Cell Value Changed");
        }
    }

    //remove procedure from list
    public void removeProcedure(Procedure procedure) {
        getSelected().removeProcedure(procedure);
        if (getSelected().getProcedures().contains(procedure)) {
            Messages.addGlobalError("procedure could not be removed");
            return;
        }

        List<Procedure> collect = getSelected()
                .getProcedures()
                .stream().filter(p -> p.getPosition() > procedure.getPosition())
                .collect(Collectors.toList());

        for (Procedure p : collect) {
            p.setPosition(p.getPosition() - 1);
        }

        Messages.addGlobalInfo("procedure removed from demonstration");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    @Override
    public AbstractRepository<Demonstration, Integer> getRepository() {
        return repository;
    }

    public void setDemonstrations(List<Demonstration> demonstrations) {
        this.demonstrations = demonstrations;
    }

    public List<Demonstration> getDemonstrations() {
        return demonstrations;
    }

    @Named
    @FacesConverter(managed = true, value = "demonstrationConverter")
    public static class DemonstrationConverter implements Converter<Demonstration> {

        @Inject
        private DemonstrationRepository repository;

        @Override
        public Demonstration getAsObject(FacesContext context, UIComponent component, String value) {
            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Demonstration value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
