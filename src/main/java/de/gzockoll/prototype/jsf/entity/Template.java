package de.gzockoll.prototype.jsf.entity;

import com.google.common.collect.ImmutableMap;
import de.gzockoll.prototype.jsf.validation.PDFDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.ProducerTemplate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
public class Template extends AbstractEntity {
    public static String DATA;

    static {
        try {
            DATA = new String(Files.readAllBytes(Paths.get("camel/vorlage/dataset.xml")), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    @Setter
    private LanguageCode languageCode = new LanguageCode();

    @OneToOne
    @NotNull
    @Setter
    private Asset transform;

    @ManyToOne
    @NotNull
    @PDFDocument
    @Setter
    private Asset stationery;

    public Template() {
    }

    public Template(String isoLanguage) {
        this.languageCode = new LanguageCode(isoLanguage);
    }

    public Template(LanguageCode language) {
        this.languageCode = language;
    }

    private TemplateState state = TemplateState.EDITABLE;

    public Template requestApproval() {
        checkState(assetsPresent());
        state = state.requestApproval();
        return this;
    }

    private boolean assetsPresent() {
        return transform != null && stationery != null;
    }

    public boolean isApproved() {
        return state == TemplateState.APPROVED;
    }

    public Template approve() {
        checkState(assetsPresent());
        state = state.approve();
        return this;
    }

    public Template assignTransform(Asset a) {
        checkNotNull(a);
        state = state.assignTransform(this, a);
        return this;
    }

    public Template assignStationary(Asset a) {
        checkNotNull(a);
        state = state.assignStationary(this, a);
        return this;
    }

    public byte[] generate(ProducerTemplate producer) {
        return generate(producer, DATA);
    }

    public byte[] generate(ProducerTemplate producer, String data) {
        checkState(isApproved(), "This template is not approved for document generation");
        return preview(producer, data);
    }

    public byte[] preview(ProducerTemplate producer, String data) {
        final Map<String, Object> headers = ImmutableMap.<String, Object>builder()
                .put("templateId", getTransform().getId())
                .put("stationeryId", getStationery().getId())
                .build();
        return (byte[]) producer.requestBodyAndHeaders(data, headers);
    }

    public byte[] preview(ProducerTemplate producer) {
        return preview(producer,DATA);
    }
}