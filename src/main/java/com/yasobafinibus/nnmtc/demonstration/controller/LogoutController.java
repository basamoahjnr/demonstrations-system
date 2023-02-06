package com.yasobafinibus.nnmtc.demonstration.controller;


import com.yasobafinibus.nnmtc.demonstration.infra.security.repository.UserActivityRepository;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.omnifaces.util.Messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;

import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventOutcome.FAILURE;
import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventOutcome.SUCCESS;
import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventType.LOGOUT;
import static java.text.MessageFormat.format;


@Named
@RequestScoped
public class LogoutController implements Serializable {


    @EJB
    private UserActivityRepository logger;
    @Inject
    private Principal principal;
    private String username;


    public String logOut() throws IOException {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
            username = principal.getName();


            //log user out
            request.logout();

            //Deal with Browser Back Bottom
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            //log user logout activity
            logger.log(this.getClass(),
                    LOGOUT,
                    SUCCESS,
                    username,
                    format("user {0} logged out",
                            username));


        } catch (ServletException ex) {
            ex.printStackTrace();
            logger.log(this.getClass(),
                    LOGOUT,
                    FAILURE,
                    username,
                    format("user {0} logged out failed",
                            username));


            Messages.addGlobalError("logout failed try again");

            // if logout error just redirect to the page the user came from
            String viewId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("from");
            FacesContext.getCurrentInstance().getExternalContext().redirect(viewId);
            FacesContext.getCurrentInstance().getExceptionHandler().handle();

        }
        return "/login?faces-redirect=true";
    }

}
