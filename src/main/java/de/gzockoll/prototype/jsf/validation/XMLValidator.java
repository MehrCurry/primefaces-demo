package de.gzockoll.prototype.jsf.validation;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class XMLValidator implements ConstraintValidator<ValidIXMLData, String> {

    private Validator validator;

    public XMLValidator() {
    }

    public XMLValidator(String ... schemata) {
        this.validator=createValidator(ImmutableSet.copyOf(schemata));
    }

    @Override
    public void initialize(ValidIXMLData validIXMLData) {
        this.validator=createValidator(ImmutableSet.copyOf(validIXMLData.schemata().split(",")));
    }

    public boolean isValid(String s) {
        try {
            validate(s);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }

    private Validator createValidator(Set<String> schemata) {
        try {
            // create a SchemaFactory capable of understanding WXS schemas
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Set<Source> sources=new HashSet<>();
            schemata.forEach(s -> {
                try {
                    if (!isEmpty(s))
                        sources.add(new StreamSource(new URL(s).openStream()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Schema schema=factory.newSchema(sources.toArray(new Source[sources.size()]));
            // create a Validator instance, which can be used to validate an instance document
            return schema.newValidator();
        } catch (SAXException  e) {
            throw new RuntimeException(e);
        }
    }

    private Source getSchemaSource(String schema ) {
        try {
            Source source=new StreamSource(new URL(schema).openStream());
            return source;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void validate(String s) throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        DocumentBuilder parser = factory.newDocumentBuilder();
        parser.setErrorHandler(new SimpleErrorHandler());
        Document document = parser.parse(new ByteArrayInputStream(s.getBytes()));
        validator.validate(new DOMSource(document));
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            validate(s);
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
            System.out.println(e);
            return false;
        }
    }

    private static class SimpleErrorHandler implements ErrorHandler {

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            log.warn("Warning: " + exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            log.error("Error: " + exception);

        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            log.error("Fatal: " + exception);
        }
    }
}
