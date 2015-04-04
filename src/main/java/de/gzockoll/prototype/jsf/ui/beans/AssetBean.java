package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.AssetService;
import de.gzockoll.prototype.jsf.entity.Asset;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkState;

@ManagedBean
@Component
@ViewScoped
public class AssetBean extends AbstractBean {

    @Autowired
    private AssetService service;

    @Getter @Setter
    private Asset selectedAsset;

    @Getter @Setter
    private UploadedFile file;

    @Getter
    private Collection<Asset> assets=null;

    public void upload() {
        if (file==null) {
            addMessage(FacesMessage.SEVERITY_WARN,"Warning","File is null");
            return;
        }
        Asset asset=new Asset(file.getContents(),file.getFileName());
        asset=service.save(asset);
        assets.add(asset);
    }

    public StreamedContent getDownload() {
        checkState(selectedAsset!=null);
        return new DefaultStreamedContent(selectedAsset.asByteStream(),selectedAsset.getMimeType(),selectedAsset.getFilename());
    }

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

    @PostConstruct
    public void init() {
        assets=service.findAll();
    }

    public void deleteAsset() {
        try {
            if (selectedAsset!=null) {
                service.delete(selectedAsset);
                assets.remove(selectedAsset);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }
}
