package de.gzockoll.prototype.jsf.entity;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable @EqualsAndHashCode
@ToString
public class TemplateGroupPK implements Serializable {
    private long tenantId;
    private LanguageCode languageCode;
    private String qualifier;

    private TemplateGroupPK() {

    }

    public TemplateGroupPK(long tenantId, LanguageCode languageCode, String qualifier) {
        this.tenantId = tenantId;
        this.languageCode = languageCode;
        this.qualifier = qualifier;
    }

    public long getTenantId() {
        return tenantId;
    }

    public LanguageCode getLanguageCode() {
        return languageCode;
    }

    public String getQualifier() {
        return qualifier;
    }
}
