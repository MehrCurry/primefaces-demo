package de.gzockoll.prototype.jsf.ui.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public abstract class AbstractBean implements Serializable {
    public void addMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, "INFO",msg);
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    protected void handleException(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        addMessage(FacesMessage.SEVERITY_ERROR, "ERROR", t.getMessage());
    }
}
