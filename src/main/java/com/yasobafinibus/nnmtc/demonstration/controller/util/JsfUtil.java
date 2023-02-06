package com.yasobafinibus.nnmtc.demonstration.controller.util;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import java.io.*;
import java.util.List;
import java.util.Objects;

import static jakarta.faces.application.FacesMessage.*;

public class JsfUtil {

    public static boolean isValidationFailed() {
        return !FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addErrorMessage(String msg, String noDetails) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_ERROR, msg, noDetails);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addWarnMessage(String msg, String noDetails) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_ERROR, msg, noDetails);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addGlobalErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_ERROR, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addGlobalSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_INFO, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addGlobalFatalMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_FATAL, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addGlobalWarnMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(SEVERITY_WARN, msg, "");
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    /**
     * @param severity FacesMessage Severity
     * @param summary message summary
     * @param details message details
     * @param global global message
     * @param clientId client id
     */
    public static void addMessage(Severity severity, String summary, String details, boolean global, String clientId) {
        FacesMessage facesMsg = new FacesMessage();
        facesMsg.setSummary(summary);
        facesMsg.setDetail(details);

        if (Objects.equals(severity, SEVERITY_INFO)) {
            facesMsg.setSeverity(SEVERITY_INFO);
        } else if (Objects.equals(severity, SEVERITY_WARN)) {
            facesMsg.setSeverity(SEVERITY_WARN);
        } else if (Objects.equals(severity, SEVERITY_ERROR)) {
            facesMsg.setSeverity(SEVERITY_ERROR);
        } else {
            facesMsg.setSeverity(SEVERITY_FATAL);
        }

        FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);

        if (global) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        }
    }


    public void throwNumberError(Object value){
       
    }

}
