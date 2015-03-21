package de.gzockoll.prototype.jsf.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Service;

@Service
public class PdfRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:xml2pdf").
                recipientList(simple("xslt:http://localhost:9090/template/${header.templateId}/transform?contentCache=false")).
                to("fop:application/pdf").to("overlayProcessor");
        from("direct:preview2pdf").
                recipientList(simple("xslt:file:///${header.tmpFile}?contentCache=false")).
                to("fop:application/pdf").to("overlayProcessor");
        from("file:camel/vorlage?noop=true").to("bean:assetController?method=fileImport");

    }
}
