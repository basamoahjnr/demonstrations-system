package com.yasobafinibus.nnmtc.demonstration.controller;

import com.yasobafinibus.nnmtc.demonstration.domain.Student;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.StudentRepository;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
@ViewScoped
public class StudentController extends AbstractController<Student> {

    @EJB
    private StudentRepository repository;


    public StudentController() {
        this.selected = new Student();
    }


    @Override
    public AbstractRepository<Student, Integer> getRepository() {
        return repository;
    }

    @Named
    @FacesConverter(managed = true, value = "studentConverter")
    public static class StudentConverter implements Converter<Student> {

        @Inject
        private StudentRepository repository;

        @Override
        public Student getAsObject(FacesContext context, UIComponent component, String value) {

            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Student value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
