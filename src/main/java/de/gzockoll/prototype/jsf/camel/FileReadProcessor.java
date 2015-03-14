package de.gzockoll.prototype.jsf.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.File;

/**
 * Created by Guido.Zockoll on 12.09.2014.
 */
public class FileReadProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        String filename=exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(new File(filename));
    }
}
