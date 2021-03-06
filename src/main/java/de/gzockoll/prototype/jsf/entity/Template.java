package de.gzockoll.prototype.jsf.entity;

import com.google.common.collect.ImmutableMap;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import de.gzockoll.prototype.jsf.validation.PDFDocument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.google.common.base.Preconditions.*;
import static java.lang.Integer.min;

@Entity
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "group")
@Getter
public class Template extends AbstractEntity {
    public static String DATA;

    @Autowired
    private transient HazelcastInstance hazelcast;

    @Autowired
    private transient TemplateRepository repository;

    static {
        try {
            DATA = new String(Files.readAllBytes(Paths.get("camel/vorlage/dataset.xml")), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ManyToOne
    private TemplateGroup group;

    @NotNull
    @Setter
    private LanguageCode languageCode = new LanguageCode();

    @NotNull
    @Lob
    // @ValidIXMLData(message = "XSL Daten ungültig")
    private String transform;

    @ManyToOne
    // @NotNull
    @PDFDocument
    @Getter
    private Asset stationery;

    Template() {
    }

    Template(TemplateGroup group, String isoCode) {
        checkArgument(group != null);
        checkArgument(isoCode != null);

        this.group = group;
        this.languageCode = new LanguageCode(isoCode);
    }

    void setGroup(TemplateGroup group) {
        checkArgument(group != null);
        this.group = group;
    }

    void setStationeryInternal(Asset stationery) {
        this.stationery = stationery;
    }

    public void setStationery(Asset stationery) {
        assignStationary(stationery);
    }

    public void setTransform(String transform) {
        assignTransform(transform);
    }

    void setTransformInternal(String transform) {
        this.transform = transform;
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
        topic().publish(new TemplateApprovalRequestedEvent(getId()));
        return this;
    }

    private ITopic topic() {
        checkState(hazelcast != null);
        return hazelcast.getTopic("template");
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
        topic().publish(new TemplateApprovedEvent(getId()));
        return this;
    }

    public Template makeEditable() {
        state = state.makeEditable();
        return this;
    }

    public Template assignTransform(String a) {
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
        byte[] bytes = preview(producer, data);
        topic().publish(new InvoiceCreatedEvent(getId(), bytes));
        return bytes;
    }

    public byte[] preview(ProducerTemplate producer, String data) {
        final Map<String, Object> headers = ImmutableMap.<String, Object>builder()
                .put("templateId", getId())
                .put("stationeryId", getStationery().getId())
                .put("random", Math.random())
                .build();
        byte[] bytes = (byte[]) producer.requestBodyAndHeaders(data, headers);
        topic().publish(new PreviewCreatedEvent(getId(), bytes));
        return bytes;
    }

    public byte[] preview(ProducerTemplate producer) {
        return preview(producer,DATA);
    }

    public String transformShort(int len) {
        return transform.substring(0, min(len, transform.length()));
    }

    public String getDisplayLanguage() {
        return languageCode.getDisplayLanguage();
    }

    public boolean isReadOnly() {
        return state!=TemplateState.EDITABLE;
    }

    public boolean isActive() {
        return group != null && this.equals(group.getActiveTemplate().orElse(null));
    }

    public boolean belongsTo(TemplateGroup group) {
        return this.group != null && this.group.equals(group);
    }

    public void save() {
        checkState(repository!=null);
        repository.save(this);
    }

    public void delete() {
        checkState(repository!=null);
        repository.delete(this);
    }
}
