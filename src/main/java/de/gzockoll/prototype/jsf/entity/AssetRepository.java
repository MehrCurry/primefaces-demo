package de.gzockoll.prototype.jsf.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface AssetRepository extends JpaRepository<Asset,Long> {
    Collection<? extends Asset> findByMimeType(String s);
}
