package de.gzockoll.prototype.jsf.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class PdfRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:xml2pdf").
                to("log:de.gzockoll.prototype.template?showAll=true&multiline=true").
                recipientList(simple("xslt:http://localhost:9090/template/${header.templateId}/transform?contentCache=false&no-cache=${header.random}")).
                to("fop:application/pdf").to("overlayProcessor");
        from("direct:preview2pdf").
                recipientList(simple("xslt:http://localhost:9090/template/${header.templateId}/transform?contentCache=false&no-cache=${header.random}")).
                to("fop:application/pdf").to("overlayProcessor");
        from("file:camel/vorlage?noop=true").to("bean:assetController?method=fileImport");

    }
}
