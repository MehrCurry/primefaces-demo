package de.gzockoll.prototype.jsf.entity;

import lombok.Data;
import lombok.experimental.Delegate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Data
public class TemplateGroup {
    @EmbeddedId
    @Delegate
    private TemplateGroupPK id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "group")
    private Collection<Template> templates=new ArrayList<>();

    @OneToOne
    private Template activeTemplate=null;

    private TemplateType type;

    private TemplateGroup() {};

    public TemplateGroup(long tenantId,String name,String language, String qualifier) {
        this.id=new TemplateGroupPK(tenantId,name,new LanguageCode(language),qualifier);
    }

    public Collection<Template> getTemplates() {
        return Collections.unmodifiableCollection(templates);
    }

    public void addTemplate(Template t) {
        checkArgument(t != null);
        checkArgument(t.belongsTo(this), "Template must be owned by this group.");
        templates.add(t);
    }

    public void activate(Template t) {
        checkArgument(templates.contains(t), "Template does not belong to this group");
        checkArgument(t.isApproved(), "Template must have been approved before activating");
        activeTemplate=t;
    }

    public Optional<Template> getActiveTemplate() {
        return Optional.ofNullable(activeTemplate);
    }

    public boolean hasSameLanguageAs(Template t) {
        checkNotNull(t);
        return t.getLanguageCode().equals(id.getLanguageCode());
    }

    public String getLabel() {
        return getName() + "[" + getLanguageCode().getDisplayLanguage() + "] " + getQualifier() + " " + type;
    }

    public Template createTemplate() {
        Template t = new Template(this, getLanguageCode().getCode());
        addTemplate(t);
        return t;
    }
}
