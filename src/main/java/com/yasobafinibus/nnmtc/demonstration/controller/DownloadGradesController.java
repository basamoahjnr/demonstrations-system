package com.yasobafinibus.nnmtc.demonstration.controller;


import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.yasobafinibus.nnmtc.demonstration.domain.GradeBook;
import com.yasobafinibus.nnmtc.demonstration.domain.GradeDto;
import com.yasobafinibus.nnmtc.demonstration.repository.GradeBookRepository;
import com.yasobafinibus.nnmtc.demonstration.util.Utils;
import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.omnifaces.util.Messages;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Named
@ViewScoped
public class DownloadGradesController implements Serializable {


    private static final long serialVersionUID = 5446821673490340406L;
    private GradeBook gradeBook;
    private StreamedContent download;


    @EJB
    private GradeBookRepository repository;


    public DownloadGradesController() {
        this.gradeBook = new GradeBook();
    }

    @SuppressWarnings("unchecked")
    public void prepDownload() throws Exception {
        List<GradeDto> gradeDtos = repository.generateGrades(getGradeBook());
        if (gradeDtos.isEmpty()) {
            Messages.addGlobalWarn("Could not generate grades for this cohort");
        } else {
            String fileName = (getGradeBook().getSchedule().getStartDate() +
                    getGradeBook().getCohort().getCode() +
                    getGradeBook().getDemonstration().getCode()).replaceAll("[._;:]","") + ".csv";
            try {
                Writer writer = null;
                writer = new FileWriter(Utils.TEMP_FOLDER + fileName);//open a file writer
                HeaderColumnNameMappingStrategy<GradeDto> strategy = new HeaderColumnNameMappingStrategyBuilder<GradeDto>().build();//tell opencsv to include headers
                strategy.setType(GradeDto.class);//we are going to use a bean class as we transform to csv
                StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)//instantiate a stateful csv writer to write bean values to
                        .withMappingStrategy(strategy)//use our strategy
                        .build();
                beanToCsv.write(gradeDtos);//write
                writer.close();//when done close

                download = getDownloadValue(Utils.TEMP_FOLDER + fileName);
                Messages.addInfo("messages", "generated file name is " + fileName);
                setGradeBook(new GradeBook());

            } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                e.printStackTrace();
                Messages.addError("messages", "file download error please try again");
            }

        }
    }

    public StreamedContent getDownloadValue(String fileName) throws Exception {
        File file = new File(fileName);
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return DefaultStreamedContent.builder()
                .name(LocalDateTime.now().toString()+".csv")
                .contentType("text/csv")
                .stream(() -> input)
                .build();
    }

    public StreamedContent getDownload() {
        return download;
    }


    public GradeBook getGradeBook() {
        return gradeBook;
    }

    public void setGradeBook(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
    }
}
