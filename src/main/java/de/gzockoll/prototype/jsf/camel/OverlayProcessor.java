package de.gzockoll.prototype.jsf.camel;

import com.google.common.io.ByteStreams;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.Overlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;

@Component
public class OverlayProcessor implements Processor {

    @Autowired
    private AssetRepository assetRepository;
    @Override
    public void process(Exchange exchange) throws Exception {
        ByteArrayOutputStream data=exchange.getIn().getBody(ByteArrayOutputStream.class);

        PDDocument inputDocument=PDDocument.load(new ByteArrayInputStream(data.toByteArray()));
        final byte[] stationeryData = assetRepository.findOne((Long) exchange.getIn().getHeader("stationeryId")).getData();
        PDDocument stationery=PDDocument.load(new ByteArrayInputStream(stationeryData));
        int pages=stationery.getNumberOfPages();

        File out= File.createTempFile("out", ".pdf");

        Overlay overlay=new Overlay();
        overlay.setInputPDF(inputDocument);
        overlay.setDefaultOverlayPDF(stationery);
        overlay.setOutputFile(out.getAbsolutePath());
        overlay.overlay(Collections.EMPTY_MAP,true);

        byte[] bytes= ByteStreams.toByteArray(new FileInputStream(out));
        out.delete();
        exchange.getIn().setBody(bytes);
    }
}
