package de.gzockoll.prototype.jsf.boundary.ui.beans;

import de.gzockoll.prototype.jsf.control.TemplateService;
import de.gzockoll.prototype.jsf.entity.*;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Collections;

@ManagedBean
@Component
@ViewScoped
public class TemplatesBean extends AbstractBean {

    @Inject
    private TemplateService service;

    @Autowired
    private TemplateFactory entityFactory;

    @Inject
    private AssetRepository assets;

    private Template selected;

    @Getter
    @Setter
    private TemplateGroup factory;

    @Getter
    @Setter
    private Template element;

    private Collection<Template> templates = Collections.EMPTY_LIST;

    private DefaultStreamedContent media;

    @Getter
    @Setter
    private String transform = "...";

    private Collection<? extends Asset> stationeries;

    @Getter
    @Setter
    private Long transformId;

    @Getter
    @Setter
    private Long stationeryId;

    @PostConstruct
    public void init() {
        element = entityFactory.createInstance();
        templates = service.findAll();
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

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            DataTable t = (DataTable) event.getSource();
            service.save((Template) t.getRowData());
        }
    }

    public Collection<Template> getTemplates() {
        return templates;
    }

    public Collection<? extends Asset> getStationeries() {
        return assets.findByMimeType("application/pdf");
    }


    public void deleteTemplate() {
        if (selected != null) {
            service.delete(selected);
            templates.remove(selected);
            selected = null;
        }
    }

    public String editTemplate() {
        return "edit";
    }

    public void previewTemplate() {
        if (selected != null) {
            try {
                byte[] data = service.preview(selected);
                media = new DefaultStreamedContent(new ByteArrayInputStream(data), "application/pdf");
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void generate() {
        if (selected != null) {
            try {
                byte[] data = service.generate(selected);
                media = new DefaultStreamedContent(new ByteArrayInputStream(data), "application/pdf");
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public DefaultStreamedContent getMedia() {
        return media;
    }

    public void reinit() {
        if (element != null) {
            // element.assignTransform(transform).assignStationary(assets.findOne(stationeryId));
            try {
                Template oldElement = element;
                element = service.save(element);
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
        element = (Template) event.getObject();
    }

    public void onRowUnselect(UnselectEvent event) {
        element = entityFactory.createInstance();
    }

    public Collection<TemplateGroup> getGroups() {
        return service.getGroups();
    }

    public void neu() {
        element = entityFactory.createInstance();
    }

    public void showDialog() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('preview').show();");

        // RequestContext.getCurrentInstance().openDialog("viewPDF");
    }

    public void requestApproval() {
        if (selected != null) {
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
        if (selected != null) {
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

    public void makeEditable() {
        if (selected != null) {
            try {
                Template old = selected;
                selected = service.makeEditable(selected);
                templates.remove(old);
                templates.add(selected);
            } catch (IllegalStateException e) {
                handleException(e);
            }
        }
    }

    public void cloneTemplate() {
        if (selected != null) {
            Template newTemplate = new Template(selected.getLanguageCode()).assignStationary(selected.getStationery()).assignTransform(selected.getTransform());
            service.save(newTemplate);
            templates.add(newTemplate);
        }
    }

    public void newTemplateForGroup() {
        if (factory != null) {
            selected = factory.createTemplate();
        }
    }

}
