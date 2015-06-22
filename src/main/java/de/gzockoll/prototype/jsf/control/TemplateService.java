package de.gzockoll.prototype.jsf.control;

import de.gzockoll.prototype.jsf.entity.Template;
import de.gzockoll.prototype.jsf.entity.TemplateGroup;
import de.gzockoll.prototype.jsf.entity.TemplateGroupRepository;
import de.gzockoll.prototype.jsf.entity.TemplateRepository;
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
import java.util.Collection;

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

    public void addTemplate(TemplateGroup group, Template t) {
        if (group != null) {
            group.addTemplate(t);
            groupRepository.save(group);
        }
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

    public Template save(Template t) {
        t.save();
        return t;
    }

    public void delete(Template t) {
        t.delete();
    }

    public Collection<Template> findAll() {
        return repository.findAll();
    }

    // TODO: create REST server class
    @RequestMapping(value = "/template/{id}/transform", method = RequestMethod.GET)
    public ResponseEntity<String> sendDocument(@PathVariable(value = "id") Long id) {
        final Template template = repository.findOne(id);
        long size=template.getTransform().length();
        log.debug("Size " + size);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setCacheControl("no-cache, must-revalidate, post-check=0, pre-check=0");
        headers.set("Pragma", "no-cache");
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

    public Template makeEditable(Template t) {
        t.makeEditable();
        return repository.save(t);
    }

    public Collection<TemplateGroup> getGroups() {
        return groupRepository.findAll();
    }
}
