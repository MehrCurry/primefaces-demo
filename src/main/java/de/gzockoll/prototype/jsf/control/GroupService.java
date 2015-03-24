package de.gzockoll.prototype.jsf.control;

import de.gzockoll.prototype.jsf.entity.GroupRepository;
import de.gzockoll.prototype.jsf.entity.TemplateGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
@Controller
@Slf4j
public class GroupService {
    @Autowired
    private GroupRepository repository;

    public Collection<TemplateGroup> findAll() {
        return repository.findAll();
    }

    public TemplateGroup save(TemplateGroup group) { repository.save(group);
        return group;
    }
}
