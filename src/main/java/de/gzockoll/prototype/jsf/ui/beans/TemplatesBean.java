package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.TemplateService;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import de.gzockoll.prototype.jsf.entity.Template;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@Component
@ViewScoped
public class TemplatesBean {

    @Inject
    private TemplateService service;

    @Inject
    private AssetRepository assets;

    @Getter @Setter
    private Template selected;

    private Collection<Template> templates=null;

    @Getter
    private DefaultStreamedContent media;

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
        if (templates==null) {
            templates = service.findAll();
            if (templates.isEmpty())
                templates.add(service.save(new Template().assignTransform(assets.findByFilename("template.xsl")).assignStationary(assets.findByFilename("stationery.pdf"))));
        }
        return templates;
    }

    public void add(ActionEvent actionEvent) {
        addMessage("Add");
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void deleteTemplate() {
        if (selected!=null) {
            service.delete(selected);
            templates.remove(selected);
            selected=null;
        }
    }

    public void previewTemplate() {
        byte[] data = service.preview(selected);
        media=new DefaultStreamedContent(new ByteArrayInputStream(data), "application/pdf");
        Map<String,Object> options = new HashMap<>();
        options.put("modal", false);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("height", 600);
        RequestContext.getCurrentInstance().openDialog("viewPDF",options,null);
    }
}
