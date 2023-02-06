package com.yasobafinibus.nnmtc.demonstration.controller;


import com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserRole;
import com.yasobafinibus.nnmtc.demonstration.infra.security.repository.UserActivityRepository;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;

import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventOutcome.*;
import static com.yasobafinibus.nnmtc.demonstration.infra.security.model.UserActivity.EventType.LOGIN;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import static java.text.MessageFormat.format;

@Named
@SessionScoped
public class LoginController implements Serializable {


    private static final long serialVersionUID = -4488462842010369363L;

    @Inject
    private SecurityContext securityContext;
    @EJB
    private UserActivityRepository logger;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private boolean rememberMe = false;
    private boolean continued = true;


    private static HttpServletResponse getResponse(FacesContext context) {
        return (HttpServletResponse) context
                .getExternalContext()
                .getResponse();
    }

    private static HttpServletRequest getRequest(FacesContext context) {
        return (HttpServletRequest) context
                .getExternalContext()
                .getRequest();
    }

    //check if user is not already logged in
    public void check() {
        Principal userPrincipal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (Objects.nonNull(userPrincipal)) {
            String path = Faces.isUserInRole("ADMIN") ? "admin" : "tutor";
            Faces.redirectPermanent(path + "/dashboard.xhtml?faces-redirect=true");
        }
    }

    public void login() {

        FacesContext context = FacesContext.getCurrentInstance();
        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        try {

            AuthenticationStatus status = securityContext.authenticate(
                    getRequest(context),
                    getResponse(context),
                    withParams()
                            .credential(credential)
                            .newAuthentication(!continued)//TODO write a mechanism to properly handle this when the remember me feature is added to the interface
                            .rememberMe(rememberMe)
            );

            switch (status) {
                case SEND_CONTINUE:
                    // Authentication mechanism has sent a redirect, should not
                    // send anything to response from JSF now.
                    context.responseComplete();
                    break;
                case SEND_FAILURE:
                    logger.log(this.getClass(),
                            LOGIN,
                            FAILURE,
                            getUsername(),
                            format("user {0} failed to logged in",
                                    getUsername()));
                    Messages.addGlobalError("Authentication failed");

                    break;
                case SUCCESS:
                    logger.log(this.getClass(),
                            LOGIN,
                            SUCCESS,
                            getUsername(),
                            format("user {0} logged in",
                                    getUsername()));
                    context.getExternalContext()
                            .redirect(context
                                    .getExternalContext()
                                    .getRequestContextPath() + redirectByUserRole() + "?faces-redirect=true");
                    break;
                case NOT_DONE:
                    logger.log(this.getClass(),
                            LOGIN,
                            NONE,
                            securityContext.getCallerPrincipal().getName(),
                            format("user {0} log in process not done",
                                    securityContext.getCallerPrincipal().getName()));

                    Messages.addGlobalError(null, "Authentication failed");

                default:
                    break;
            }
        } catch (IOException ex) {
            context.getExceptionHandler().handle();
        }

    }

    //redirect user based on the role the user has
    private String redirectByUserRole() {

        String dashboardUrl = "/dashboard";
        String adminUrl = "/admin/dashboard";
        String tutorUrl = "/tutor/dashboard";

        String path = dashboardUrl;

        if (securityContext.isCallerInRole(UserRole.ADMIN.toString())) {
            path = adminUrl;
        }
        if (securityContext.isCallerInRole(UserRole.TUTOR.toString())) {
            path = tutorUrl;
        }
        return path;
    }

    public @NotEmpty String getUsername() {
        return this.username;
    }

    public void setUsername(@NotEmpty String username) {
        this.username = username;
    }

    public @NotEmpty String getPassword() {
        return this.password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return this.rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public boolean isContinued() {
        return this.continued;
    }

    public void setContinued(boolean continued) {
        this.continued = continued;
    }
}
