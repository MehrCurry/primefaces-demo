package de.gzockoll.prototype.jsf.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity @EqualsAndHashCode
@ToString
public class TemplateGroup {
    @EmbeddedId
    private TemplateGroupPK id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Template> templates=new ArrayList<>();

    @OneToOne
    private Template activeTemplate=null;

    private TemplateGroup() {};

    public TemplateGroup(long tenantId,String language, String qualifier) {
        this.id=new TemplateGroupPK(tenantId,new LanguageCode(language),qualifier);
    }

    public void addTemplate(Template t) {
        checkArgument(hasSameLanguageAs(t), "Language mismatch: " + id.getLanguageCode() + "<>" + t.getLanguageCode());
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
}
