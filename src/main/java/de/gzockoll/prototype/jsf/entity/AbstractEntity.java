package de.gzockoll.prototype.jsf.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
public abstract class AbstractEntity extends ValidateableObject {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    public void beforeSave() {
        createdAt=new Date();
    }

    @PreUpdate
    public void beforeUpdate() { updatedAt=new Date();
    }
}
