package de.gzockoll.prototype.jsf.camel;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PageExtractor;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

public class OverlayProcessorTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();
    private PDDocument content;
    private PDDocument stationery;


    @After
    public void tearDown() throws IOException {
        content.close();
        stationery.close();
    }

    @Test
    public void testOneOnThree() throws IOException, COSVisitorException {
        OverlayProcessor proc=new OverlayProcessor();

        content = createDocument(1, "Content", 1);
        stationery = createDocument(3, "Stationery",2);
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor("JUnit");
        stationery.setDocumentInformation(info);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(containsString("stationery must have one or two pages"));
        PDDocument result = proc.overlay(content, stationery);
    }


    @Test
    public void testOneOnOne() throws IOException, COSVisitorException {
        OverlayProcessor proc=new OverlayProcessor();

        content = createDocument(1, "Content", 1);
        stationery = createDocument(1, "Stationery",2);
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor("JUnit");
        stationery.setDocumentInformation(info);

        PDDocument result = proc.overlay(content, stationery);
        assertThat(result.getDocumentInformation().getAuthor()).isEqualTo("JUnit");
        assertThat(result.getNumberOfPages()).isEqualTo(1);
        final String text = getText(extractPage(result, 1));
        assertThat(text).contains("Content on page #1");
        assertThat(text).contains("Stationery on page #1");
    }

    @Test
    public void testOneOnTwo() throws IOException, COSVisitorException {
        OverlayProcessor proc=new OverlayProcessor();

        content = createDocument(1, "Content", 1);
        stationery = createDocument(2, "Stationery",2);
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor("JUnit");
        stationery.setDocumentInformation(info);

        PDDocument result = proc.overlay(content, stationery);
        assertThat(result.getDocumentInformation().getAuthor()).isEqualTo("JUnit");
        assertThat(result.getNumberOfPages()).isEqualTo(1);

        final String text = getText(extractPage(result, 1));
        assertThat(text).contains("Content on page #1");
        assertThat(text).contains("Stationery on page #1");
    }

    @Test
    public void testTwoOnOne() throws IOException, COSVisitorException {
        OverlayProcessor proc=new OverlayProcessor();

        content = createDocument(2, "Content", 1);
        stationery = createDocument(1, "Stationery",2);
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor("JUnit");
        stationery.setDocumentInformation(info);

        PDDocument result = proc.overlay(content, stationery);
        assertThat(result.getDocumentInformation().getAuthor()).isEqualTo("JUnit");
        assertThat(result.getNumberOfPages()).isEqualTo(2);

        String text = getText(extractPage(result, 1));
        assertThat(text).contains("Content on page #1");
        assertThat(text).contains("Stationery on page #1");

        text = getText(extractPage(result, 2));
        assertThat(text).contains("Content on page #2");
        assertThat(text).contains("Stationery on page #1");
    }

    @Test
    public void testFiveOnTwo() throws IOException, COSVisitorException {
        OverlayProcessor proc=new OverlayProcessor();

        content = createDocument(5, "Content", 1);
        stationery = createDocument(2, "Stationery",2);
        PDDocumentInformation info = new PDDocumentInformation();
        info.setAuthor("JUnit");
        stationery.setDocumentInformation(info);

        PDDocument result = proc.overlay(content, stationery);
        assertThat(result.getDocumentInformation().getAuthor()).isEqualTo("JUnit");
        assertThat(result.getNumberOfPages()).isEqualTo(5);
        String text = getText(extractPage(result, 1));
        assertThat(text).contains("Content on page #1");
        assertThat(text).contains("Stationery on page #1");

        text = getText(extractPage(result, 2));
        assertThat(text).contains("Content on page #2");
        assertThat(text).contains("Stationery on page #2");

        text = getText(extractPage(result, 3));
        assertThat(text).contains("Content on page #3");
        assertThat(text).contains("Stationery on page #2");

        text = getText(extractPage(result, 4));
        assertThat(text).contains("Content on page #4");
        assertThat(text).contains("Stationery on page #2");

        text = getText(extractPage(result, 5));
        assertThat(text).contains("Content on page #5");
        assertThat(text).contains("Stationery on page #2");
    }

    private PDDocument createDocument(int pages, String someText, int line) throws IOException {
        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        for (int i = 1; i <= pages; i++) {

            PDPage page = new PDPage();
            document.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount(100, 700 + 20 * line);
            contentStream.drawString(someText + " on page #" + i);
            contentStream.endText();

            contentStream.close();
        }
        return document;
    }

    private PDDocument extractPage(PDDocument doc,int page) throws IOException {
        PageExtractor extractor=new PageExtractor(doc,page,page);
        return extractor.extract();
    }

    private String getText(PDDocument doc) throws IOException {
        PDFTextStripper stripper=new PDFTextStripper();
        return stripper.getText(doc);
    }


}