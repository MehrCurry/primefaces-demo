package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.TemplateService;
import de.gzockoll.prototype.jsf.entity.Asset;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import de.gzockoll.prototype.jsf.entity.Template;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Collection;

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

    private Collection<Template> templates=null;

    private DefaultStreamedContent media;

    private Collection<? extends Asset> transformers;

    private Collection<? extends Asset> stationeries;

    @Getter @Setter
    private Long transformId;

    @Getter @Setter
    private Long stationeryId;

    @PostConstruct
    public void init() {
        element=new Template("us");
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

    public Collection<? extends Asset> getTransformers() {
        return assets.findByMimeType("application/xslt+xml");
    }

    public Collection<? extends Asset> getStationeries() {
        return assets.findByMimeType("application/pdf");
    }

    public void addMessage(String msg) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg,  null);
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
            byte[] data = service.preview(selected);
            media=new DefaultStreamedContent(new ByteArrayInputStream(data), "application/pdf");
            return media;
        } else
            return null;
    }

    public DefaultStreamedContent getMedia() {
        return media;
    }

    public String reinit() {
        if (element!=null) {
            element.assignTransform(assets.findOne(transformId)).assignStationary(assets.findOne(stationeryId));
            element=service.save(element);
            addMessage("Saved");
        }
        element=new Template();
        return null;
    }
}
