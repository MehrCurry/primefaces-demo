package de.gzockoll.prototype.jsf.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateGroupRepository extends CrudRepository<TemplateGroup, TemplateGroupPK> {
}
