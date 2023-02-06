package com.yasobafinibus.nnmtc.demonstration.controller;

import com.yasobafinibus.nnmtc.demonstration.domain.Tutor;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.TutorRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.ResourceBundle;

import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserRole.TUTOR;

@Named
@RequestScoped
public class TutorController extends AbstractController<Tutor> {

    @EJB
    private TutorRepository repository;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private PasswordHash passwordHash;
    private List<Tutor> tutors;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
            message = "{org.yasobafinibus.validator.Password.message}")
    private String password;

    @PostConstruct
    public void init() {
        tutors = getRepository().findAll();
    }

    public TutorController() {
        this.selected = new Tutor();
    }

    @Override
    public void create() {
        getSelected().getUser().setUsername(getSelected().getEmail());
        getSelected().getUser().setPassword(passwordHash.generate(password.toCharArray()));
        getSelected().getUser().addRoles(TUTOR);

        super.persist(PersistAction.CREATE,
                ResourceBundle.getBundle("/Bundle").getString(getSelected().getClass().getSimpleName() + "Created"),
                getSelected());
    }


    @Override
    public AbstractRepository<Tutor, Integer> getRepository() {
        return repository;
    }

    public List<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(List<Tutor> tutors) {
        this.tutors = tutors;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Named
    @FacesConverter(managed = true, value = "tutorConverter")
    public static class TutorConverter implements Converter<Tutor> {

        @EJB
        private TutorRepository repository;

        @Override
        public Tutor getAsObject(FacesContext context, UIComponent component, String value) {
            try {
                return repository.find(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Tutor value) {
            return value == null ? "" : String.valueOf(value.getId());
        }
    }
}
