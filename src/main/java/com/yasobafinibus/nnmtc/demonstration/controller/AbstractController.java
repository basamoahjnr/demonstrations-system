package com.yasobafinibus.nnmtc.demonstration.controller;

import com.yasobafinibus.nnmtc.demonstration.domain.AbstractEntity;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import jakarta.enterprise.context.Dependent;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.omnifaces.util.Messages;

import java.io.Serializable;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public abstract class AbstractController<E extends AbstractEntity> implements Serializable {


    protected E selected;

    public void create() {
        persist(PersistAction.CREATE,
                ResourceBundle.getBundle("/Bundle").getString(selected.getClass().getSimpleName() + "Created"),
                selected);
    }

    public void update() {
        persist(PersistAction.UPDATE,
                ResourceBundle.getBundle("/Bundle").getString(selected.getClass().getSimpleName() + "Edited"),
                selected);
    }

    public void destroy() {
        persist(PersistAction.DELETE,
                ResourceBundle.getBundle("/Bundle").getString(selected.getClass().getSimpleName() + "Deleted"),
                selected);
    }

    void persist(PersistAction persistAction,
                 String successMessage,
                 E entity) {
        if (entity != null) {
            try {

                switch (persistAction) {
                    case UPDATE:
                        getRepository().edit(entity);
                        break;
                    case DELETE:
                        getRepository().remove(entity);
                        break;
                    default:
                        getRepository().create(entity);
                        selected = null;
                }
                Messages.addGlobalInfo(successMessage);
            } catch (Exception ex) {

                Throwable rootCause = ExceptionUtils.getRootCause(ex);
                //if database throws a duplicate error
                if (rootCause.getMessage().contains("duplicate")) {

                    Pattern compile = Pattern.compile("\\s*=\\s*([\\S\\s]+)");
                    Matcher matcher = compile.matcher(rootCause.getMessage());
                    if(matcher.find()){
                        Messages.addError("messages",
                                ResourceBundle.getBundle("/Bundle").getString("DuplicateErrorMessage"),
                               matcher.group(1));
                    }

                } else {
                    Messages.addGlobalError(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccurred"));
                }
                ex.printStackTrace();
            }
        }
    }

    public E getSelected() {
        return selected;
    }

    public void setSelected(E selected) {
        this.selected = selected;
    }

    public abstract AbstractRepository<E, Integer> getRepository();


    public enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }
}
