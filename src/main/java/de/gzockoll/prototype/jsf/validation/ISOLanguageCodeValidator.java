package de.gzockoll.prototype.jsf.validation;

import com.google.common.collect.Sets;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Locale;

public class ISOLanguageCodeValidator implements ConstraintValidator<ValidISOLanguageCode,String> {
    public static final HashSet<String> VALID_LANGUAGES = Sets.newHashSet(Locale.getISOLanguages());
    private boolean nullIsValid;

    @Override
    public void initialize(ValidISOLanguageCode validISOLanguageCode) {
        this.nullIsValid=validISOLanguageCode.allowNull();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return (s==null && nullIsValid) || VALID_LANGUAGES.contains(s);
    }

    boolean isValid(String s) {
        return isValid(s,null);
    }
}
