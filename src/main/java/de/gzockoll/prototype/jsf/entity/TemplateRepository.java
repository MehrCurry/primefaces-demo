package de.gzockoll.prototype.jsf.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    public List<Template> findByLanguageCode(String language);
}
