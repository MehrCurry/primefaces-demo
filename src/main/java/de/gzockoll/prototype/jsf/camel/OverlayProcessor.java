package de.gzockoll.prototype.jsf.camel;

import de.gzockoll.prototype.jsf.entity.AssetRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.Overlay;
import org.apache.pdfbox.util.PageExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

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

        PDDocument result = overlay(inputDocument, stationery);
        ByteArrayOutputStream bytes=new ByteArrayOutputStream();
        result.save(bytes);
        exchange.getIn().setBody(bytes.toByteArray());
    }

    PDDocument overlay(PDDocument inputDocument, PDDocument stationery) throws IOException, COSVisitorException {
        File out= File.createTempFile("out", ".pdf");
        out.deleteOnExit();
        Overlay overlay=new Overlay();
        overlay.setInputPDF(inputDocument);

        List<PDPage> pages = stationery.getDocumentCatalog().getAllPages();
        pages.forEach(p -> {
            System.out.println("Orientation: " + p.findRotation());
            p.setRotation(0);
        });
        int pageCount=stationery.getNumberOfPages();

        switch (pageCount) {
            case 1:
                overlay.setDefaultOverlayPDF(stationery);
                break;
            case 2:
                PageExtractor extractor=new PageExtractor(stationery);
                extractor.setStartPage(1);
                extractor.setEndPage(1);
                PDDocument page1 = extractor.extract();
                extractor.setStartPage(2);
                extractor.setEndPage(2);
                PDDocument page2 = extractor.extract();
                overlay.setFirstPageOverlayPDF(page1);
                overlay.setDefaultOverlayPDF(page2);
                break;
            default:
                throw new IllegalArgumentException("stationery must have one or two pages");
        }

        PDDocumentInformation info = stationery.getDocumentInformation();
        overlay.setOutputFile(out.getAbsolutePath());
        overlay.overlay(Collections.EMPTY_MAP,true);
        PDDocument result = PDDocument.load(out);
        info.setCreationDate(Calendar.getInstance());
        result.setDocumentInformation(info);
        return result;
    }
}
