package de.gzockoll.prototype.jsf.boundary.ui.converter;

import de.gzockoll.prototype.jsf.entity.LanguageCode;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = LanguageCode.class)
@Component
public class LanguageCodeConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return new LanguageCode(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return ((LanguageCode) o).getCode();
    }
}
