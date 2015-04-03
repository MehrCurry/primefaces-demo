package de.gzockoll.prototype.jsf.entity;


import lombok.Data;

@Data
public class InvoiceCreatedEvent extends TemplateEvent {
    private final byte[] bytes;

    public InvoiceCreatedEvent(Long id, byte[] bytes) {
        super(id);
        this.bytes=bytes;
    }
}
