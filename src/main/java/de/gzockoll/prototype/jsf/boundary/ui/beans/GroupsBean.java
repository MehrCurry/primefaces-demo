package de.gzockoll.prototype.jsf.boundary.ui.beans;

import de.gzockoll.prototype.jsf.control.GroupService;
import de.gzockoll.prototype.jsf.entity.TemplateGroup;
import de.gzockoll.prototype.jsf.entity.TemplateType;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import java.util.Collection;
import java.util.Locale;

@ManagedBean
@Component
@ViewScoped
public class GroupsBean extends AbstractBean {
    @Autowired
    private GroupService service;

    @Getter @Setter
    private TemplateGroup selected;

    @Getter @Setter
    private TemplateGroup element=new TemplateGroup(0, "", Locale.getDefault().getLanguage(),"");

    @Getter
    private Collection<TemplateGroup> groups;

    @Getter
    private TemplateType[] types= TemplateType.values();

    @PostConstruct
    public void init() {
        groups=service.findAll();
    }

    public void save() {
        if (element!=null) {
            try {
                TemplateGroup old = element;
                element=service.save(element);
                groups.remove(old);
                groups.add(element);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    public void neu() {
        element=new TemplateGroup(0, "", Locale.getDefault().getLanguage(),"");
    }

    public void onRowSelect(SelectEvent event) {
        element = (TemplateGroup) event.getObject();
    }

    public void onRowUnselect(UnselectEvent event) {
        neu();
    }

    public void delete() {
        if (element != null) {
            try {
                service.delete(element);
                groups.remove(element);
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

}
