package de.gzockoll.prototype.jsf.ui.beans;

import de.gzockoll.prototype.jsf.control.AssetService;
import de.gzockoll.prototype.jsf.entity.Asset;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

@ManagedBean
@Component
@ViewScoped
@Getter
@Setter
public class AssetBean {

    @Inject
    private AssetService service;

    private Collection<Asset> assets= Collections.EMPTY_LIST;

    public Collection<Asset> refresh() {
        assets=service.findAll();
        return assets;
    }
}
