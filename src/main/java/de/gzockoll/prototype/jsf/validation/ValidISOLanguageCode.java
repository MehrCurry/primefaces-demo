package de.gzockoll.prototype.jsf.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy=ISOLanguageCodeValidator.class)
public @interface ValidISOLanguageCode {
    String message() default "{de.gzockoll.prototype.templates.validation.invalidCountryCode}";

    Class[] groups() default {};

    Class[] payload() default {};
}
