package de.gzockoll.prototype.jsf.entity;

import de.gzockoll.prototype.jsf.validation.ValidISOLanguageCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Locale;

@Embeddable
@EqualsAndHashCode(callSuper = false)
@Getter
@ToString
public class LanguageCode extends ValidateableObject implements Serializable {
    @ValidISOLanguageCode
    private String code;

    public LanguageCode() {
        this.code= Locale.getDefault().getLanguage();
    }

    public LanguageCode(@ValidISOLanguageCode String code) {
        this.code = code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayLanguage() {
        return Locale.forLanguageTag(code).getDisplayLanguage().toLowerCase();
    }
}
