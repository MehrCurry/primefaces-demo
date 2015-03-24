package de.gzockoll.prototype.jsf.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<TemplateGroup,Long> {
}
