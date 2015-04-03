package de.gzockoll.prototype.jsf.entity;

import org.springframework.stereotype.Component;

@Component
public class TemplateFactory extends AutowiringEntityFactory<Template> {
    @Override
    protected Template createNewInstance() {
        return new Template();
    }
}
