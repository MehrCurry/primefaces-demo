package de.gzockoll.prototype.jsf.boundary.ui.converter;

import de.gzockoll.prototype.jsf.entity.AbstractEntity;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.List;

@SuppressWarnings("unchecked")
@Component("entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        AbstractEntity ret = null;
        UIComponent src = arg1;
        if (src != null) {
            List<UIComponent> childs = src.getChildren();
            UISelectItems itens = null;
            if (childs != null) {
                for (UIComponent ui : childs) {
                    if (ui instanceof UISelectItems) {
                        itens = (UISelectItems) ui;
                        break;
                    } else if (ui instanceof UISelectItem) {
                        UISelectItem item = (UISelectItem) ui;
                        try {
                            AbstractEntity val = (AbstractEntity) item.getItemValue();
                            if (arg2.equals("" + val.getId())) {
                                ret = val;
                                break;
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }

            if (itens != null) {
                List<AbstractEntity> values = (List<AbstractEntity>) itens.getValue();
                if (values != null) {
                    for (AbstractEntity val : values) {
                        if (arg2.equals("" + val.getId())) {
                            ret = val;
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String ret = "";
        if (arg2 != null && arg2 instanceof AbstractEntity) {
            AbstractEntity m = (AbstractEntity) arg2;
            if (m != null) {
                Long id = m.getId();
                if (id != null) {
                    ret = id.toString();
                }
            }
        }
        return ret;
    }
}
