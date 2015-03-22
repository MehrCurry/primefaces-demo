package de.gzockoll.prototype.jsf.control;

import com.google.common.collect.ImmutableMap;
import de.gzockoll.prototype.jsf.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Transactional
@Controller
@Slf4j
public class TemplateService {

    @EndpointInject(uri = "direct:xml2pdf")
    ProducerTemplate producer;
    @EndpointInject(uri = "direct:preview2pdf")
    ProducerTemplate previewProducer;
    @Autowired
    private TemplateRepository repository;
    @Autowired
    private TemplateGroupRepository groupRepository;

    public TemplateGroup addTemplate(long tenantId, String language, String qualifier, Template t) {
        repository.save(t);
        TemplateGroupPK pk = new TemplateGroupPK(tenantId, new LanguageCode(language), qualifier);
        TemplateGroup group = groupRepository.findOne(pk);
        if (group == null) {
            group = new TemplateGroup(tenantId, language, qualifier);
        }

        group.addTemplate(t);
        return groupRepository.save(group);
    }

    public void updateTemplate(Template t) {
        repository.save(t);
    }

    public byte[] generate(Template t) {
        checkArgument(t != null);
        return t.generate(producer);
    }

    public byte[] preview(Template t) {
        checkArgument(t != null);
        return t.preview(producer);
    }

    public byte[] preview(String xslt, Asset stationery) {
        Path tmpFile = null;
        try {
            String data = new String(Files.readAllBytes(Paths.get("camel/vorlage/long-dataset.xml")), Charset.forName("UTF-8"));
            tmpFile = Files.createTempFile("tm", ".xslt");
            Files.write(tmpFile, xslt.getBytes());
            final Map<String, Object> headers = ImmutableMap.<String, Object>builder()
                    .put("tmpFile", tmpFile.toFile().getCanonicalPath())
                    .put("stationeryId", stationery.getId())
                    .build();
            return (byte[]) previewProducer.requestBodyAndHeaders(data, headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmpFile != null)
                tmpFile.toFile().delete();
        }
    }

    public Collection<? extends Template> findAllTemplates() {
        return repository.findAll();
    }

    public Template save(Template bean) {
        return repository.save(bean);
    }

    public void delete(Template t) {
        repository.delete(t);
    }

    public Collection<Template> findAll() {
        return repository.findAll();
    }

    @RequestMapping(value = "/template/{id}/transform", method = RequestMethod.GET)
    public ResponseEntity<String> sendDocument(@PathVariable(value = "id") Long id) {
        final Template template = repository.findOne(id);
        long size=template.getTransform().length();
        log.debug("Size " + size);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<String> response = new ResponseEntity<>(template.getTransform(), headers, HttpStatus.OK);
        return response;
    }

    public Template requestApproval(Template t) {
        t.requestApproval();
        return repository.save(t);
    }

    public Template approve(Template t) {
        t.approve();
        return repository.save(t);
    }
}
