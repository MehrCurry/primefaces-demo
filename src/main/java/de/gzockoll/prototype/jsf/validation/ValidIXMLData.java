package de.gzockoll.prototype.jsf.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy=ISOLanguageCodeValidator.class)
public @interface ValidIXMLData {
    String message() default "{de.gzockoll.prototype.templates.validation.invalidXML}";

    Class[] groups() default {};

    Class[] payload() default {};

    String schemata() default "";
}
