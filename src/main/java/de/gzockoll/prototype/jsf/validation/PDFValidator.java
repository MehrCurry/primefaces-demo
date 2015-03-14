package de.gzockoll.prototype.jsf.validation;

import com.google.common.collect.Sets;
import de.gzockoll.prototype.jsf.entity.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;

@Slf4j
public class PDFValidator implements ConstraintValidator<PDFDocument,Asset> {
    public static final HashSet<String> VALID_LANGUAGES = Sets.newHashSet(Locale.getISOLanguages());

    @Override
    public void initialize(PDFDocument pdfDocument) {

    }

    @Override
    public boolean isValid(Asset asset, ConstraintValidatorContext constraintValidatorContext) {
        if (asset==null) {
            return false;
        }
        try {
            PDDocument doc=PDDocument.load(asset.asByteStream());
            return doc!=null;
        } catch (IOException e) {
            if (constraintValidatorContext!=null) {
                log.debug(e.getMessage());
                constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage());
            }
            return false;
        }
    }
}
