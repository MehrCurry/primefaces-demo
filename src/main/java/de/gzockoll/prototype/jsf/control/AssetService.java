package de.gzockoll.prototype.jsf.control;

import de.gzockoll.prototype.jsf.entity.Asset;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import de.gzockoll.prototype.jsf.entity.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Transactional
@Controller
@Slf4j
public class AssetService {
    @Autowired
    private AssetRepository repository;

    public Collection<? extends Asset> findByMimeType(String s) {
        return repository.findByMimeType(s);
    }

    public Collection<Asset> findAll() {
        return repository.findAll();
    }

    public Asset save(Asset asset) {
        repository.save(asset);
        return asset;
    }

    public Asset save(InvoiceCreatedEvent event) {
        UUID uuid=UUID.randomUUID();
        String name=String.format("%Y%m%d-%H%M-INV-%s", new Date(), uuid.toString().substring(0,8));

        Asset asset=new Asset(event.getBytes(),name);
        return repository.save(asset);
    }

    public void delete(Asset asset) {
        repository.delete(asset);
    }
}
