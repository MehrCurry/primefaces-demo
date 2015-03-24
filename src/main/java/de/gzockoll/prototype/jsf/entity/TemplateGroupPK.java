package de.gzockoll.prototype.jsf.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable @EqualsAndHashCode
@ToString
@Data @Builder
public class TemplateGroupPK implements Serializable {
    private long tenantId;
    private String name;
    private LanguageCode languageCode;
    private String qualifier;

    private TemplateGroupPK() {

    }

    public TemplateGroupPK(long tenantId, String name,LanguageCode languageCode, String qualifier) {
        this.tenantId = tenantId;
        this.name=name;
        this.languageCode = languageCode;
        this.qualifier = qualifier;
    }
}
