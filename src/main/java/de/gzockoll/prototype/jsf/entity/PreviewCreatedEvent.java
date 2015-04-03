package de.gzockoll.prototype.jsf.entity;

import lombok.Data;

@Data
public class PreviewCreatedEvent extends TemplateEvent {
    private final byte[] bytes;

    public PreviewCreatedEvent(Long id, byte[] bytes) {
        super(id);
        this.bytes=bytes;
    }
}
