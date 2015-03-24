package de.gzockoll.prototype.jsf.entity;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.capitalize;

/**
 * Created by Guido on 01.03.2015.
 */
public enum TemplateType {
    /**
     * Template is used for generating an sepa bankfile email.
     */
    SEPA_BANKFILE_EMAIL(2, true),

    /**
     * A PDF background that can be merged with another PDF.
     */
    STATIONERY(7, false),

    /**
     * Common XSL Stylesheet use it for all FinanceGate internal e.g. Oxseed XML file generation.
     */
    COMMON_XSL(10, true),

    /**
     * Mandate templates. For example for SEPA mandates.
     */
    SEPA_MANDATE(11, true),

    /**
     * SEPA accompanying ticket templates.
     */
    SEPA_ACCOMPANYING_TICKET(12, true),

    /**
     * SEPA container templates.
     */
    SEPA_CONTAINER(13, true),

    /**
     * SEPA debit templates.
     */
    SEPA_DEBIT(14, true),

    /**
     * SEPA credit templates.
     */
    SEPA_CREDIT(15, true),

    /**
     * Template for creating Invoices.
     */
    INVOICE(16, true);

    /**
     * Registry path where template settings are located.
     */
    public static final String SETTINGS_REGISTRY_PATH = "/merchants/templates/settings";

    /**
     * Returns the template type that has the given id.
     *
     * @param templateTypeId
     *            identifier of the template type
     * @return the template type
     *
     *         throws IllegalArgumentException if no <code>TemplateType</code> with the given ID is defined
     */
    public static TemplateType getTemplateTypeById(final int templateTypeId) {
        for (final TemplateType templateType : values()) {
            if (templateType.getId() == templateTypeId) {
                return templateType;
            }
        }
        throw new IllegalArgumentException("TemplateType with id '" + templateTypeId + "' not found.");
    }

    /**
     * The unique id of this enumeration.
     */
    private final int id;

    /**
     * Define the content of the template.
     */
    private final boolean xmlTemplate;

    /**
     * Create a new TemplateType.
     *
     * @param id
     *            unique id of the template type
     * @param xmlTemplate
     *            defines the content of the template
     */
    private TemplateType(final int id, boolean xmlTemplate) {
        this.id = id;
        this.xmlTemplate = xmlTemplate;
    }

    /**
     * Get the id. See {@link #id} for detailed documentation
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for {@link #xmlTemplate}
     *
     * @return the xmlTemplate
     */
    public boolean isXmlTemplate() {
        return xmlTemplate;
    }

    @Override
    public String toString() {
        return Arrays.asList(name().split("_")).stream().map(s -> capitalize(s.toLowerCase())).collect(Collectors.joining(" "));
    }
}
