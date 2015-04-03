package de.gzockoll.prototype.jsf.entity;

public class TemplateEvent implements DomainEvent {
    protected final Long template;

    public TemplateEvent(Long template) {
        this.template=template;
    }
}
