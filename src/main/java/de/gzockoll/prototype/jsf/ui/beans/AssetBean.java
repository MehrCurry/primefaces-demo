package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.AssetService;
import de.gzockoll.prototype.jsf.entity.Asset;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Collection;

@ManagedBean
@Component
@ViewScoped
public class AssetBean {

    @Inject
    private AssetService service;

    @Getter @Setter
    private Asset selectedAsset;

    private Collection<Asset> assets=null;


    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            DataTable t= (DataTable) event.getSource();
            service.save((Asset) t.getRowData());
        }
    }

    public Collection<Asset> getAssets() {
        if (assets==null)
            assets=service.findAll();
        return assets;
    }
}
