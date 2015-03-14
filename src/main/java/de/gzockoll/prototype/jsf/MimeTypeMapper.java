package de.gzockoll.prototype.jsf;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MimeTypeMapper implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("xsd", "text/xml; charset=utf-8");
        mappings.add("eot", "application/vnd.ms-fontobject; charset=utf-8");
        mappings.add("otf", "font/opentype; charset=utf-8");
        mappings.add("ttf", "application/x-font-ttf; charset=utf-8");
        mappings.add("woff", "application/x-font-woff; charset=utf-8");
        mappings.add("svg", "image/svg+xml; charset=utf-8");
        mappings.add("ico", "image/x-icon; charset=utf-8");
        container.setMimeMappings(mappings);
    }
}
