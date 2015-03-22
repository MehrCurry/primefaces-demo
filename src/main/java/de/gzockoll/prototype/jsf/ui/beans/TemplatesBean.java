package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.TemplateService;
import de.gzockoll.prototype.jsf.entity.Asset;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import de.gzockoll.prototype.jsf.entity.Template;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@ManagedBean
@Component
@ViewScoped
public class TemplatesBean implements Serializable {

    @Inject
    private TemplateService service;

    @Inject
    private AssetRepository assets;

    private Template selected;

    @Getter @Setter
    private Template element;

    private Collection<Template> templates= Collections.EMPTY_LIST;

    private DefaultStreamedContent media;

    @Getter @Setter
    private String transform="...";

    private Collection<? extends Asset> stationeries;

    @Getter @Setter
    private Long transformId;

    @Getter @Setter
    private Long stationeryId;

    @PostConstruct
    public void init() {
        element=new Template();
        templates=service.findAll();
    }

    public Template getSelected() {
        return selected;
    }

    public void setSelected(Template selected) {
        this.selected = selected;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            DataTable t= (DataTable) event.getSource();
            service.save((Template) t.getRowData());
        }
    }

    public Collection<Template> getTemplates() {
        return templates;
    }

    public Collection<? extends Asset> getStationeries() {
        return assets.findByMimeType("application/pdf");
    }


    public void addMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO,msg);
    }

    public void addMessage(FacesMessage.Severity severity,String msg) {
        FacesMessage message = new FacesMessage(severity, msg,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void deleteTemplate() {
        if (selected!=null) {
            service.delete(selected);
            templates.remove(selected);
            selected=null;
        }
    }

    public String editTemplate() {
        return "edit";
    }

    public DefaultStreamedContent previewTemplate() {
        if (selected!=null) {
            try {
                byte[] data = service.preview(selected);
                media=new DefaultStreamedContent(new ByteArrayInputStream(data), "application/pdf");
                showDialog();
                return media;
            } catch (Exception e) {
                handleException(e);
                return null;
            }
        } else
            return null;
    }

    public DefaultStreamedContent getMedia() {
        return media;
    }

    public void reinit() {
        if (element!=null) {
            // element.assignTransform(transform).assignStationary(assets.findOne(stationeryId));
            try {
                Template oldElement = element;
                element=service.save(element);
                templates.remove(oldElement);
                templates.add(element);
                addMessage("Saved");
            } catch (ConstraintViolationException e) {
                addMessage(e.getMessage());
                templates.remove(element);
            }
        }
    }

    public void onRowSelect(SelectEvent event) {
        element= (Template) event.getObject();
    }

    public void onRowUnselect(UnselectEvent event) {
        element=new Template();
    }

    public void neu() {
        element=new Template();
    }
    public void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('preview').show();");

        // RequestContext.getCurrentInstance().openDialog("viewPDF");
    }

    public void requestApproval() {
        if (selected!=null) {
            try {
                Template old = selected;
                selected = service.requestApproval(selected);
                templates.remove(old);
                templates.add(selected);
            } catch (IllegalStateException e) {
                handleException(e);
            }
        }
    }

    public void approve() {
        if (selected!=null) {
            try {
                Template old = selected;
                selected = service.approve(selected);
                templates.remove(old);
                templates.add(selected);
            } catch (IllegalStateException e) {
                handleException(e);
            }
        }
    }

    private void handleException(Throwable t) {
        while (t.getCause() !=null) {
            t=t.getCause();
        }
        addMessage(FacesMessage.SEVERITY_ERROR, t.getMessage());
    }
}
