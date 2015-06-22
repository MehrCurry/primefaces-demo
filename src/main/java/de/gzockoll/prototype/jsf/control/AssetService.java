package de.gzockoll.prototype.jsf.control;

import com.hazelcast.core.HazelcastInstance;
import de.gzockoll.prototype.jsf.entity.Asset;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import de.gzockoll.prototype.jsf.entity.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Transactional
@Controller("assetService")
@Slf4j
public class AssetService {
    @Autowired
    private AssetRepository repository;

    @Autowired
    private HazelcastInstance hazelcastInstance;

    public Collection<? extends Asset> findByMimeType(String s) {
        return repository.findByMimeType(s);
    }

    public Collection<Asset> findAll() {
        return repository.findAll();
    }


    @PostConstruct
    public void subscribe() {
        hazelcastInstance.getTopic("template").addMessageListener(message -> {
            if (message.getMessageObject() instanceof  InvoiceCreatedEvent) {
                saveInvoice((InvoiceCreatedEvent) message.getMessageObject());
            }
        });
    }

    public Asset save(Asset asset) {
        repository.save(asset);
        return asset;
    }

    public Asset saveInvoice(InvoiceCreatedEvent event) {
        UUID uuid=UUID.randomUUID();
        String name=String.format("%1$tY%1$tm%1$td-%1$tH%1$tM-INV-%2$s.pdf", new Date(), uuid.toString().substring(0,8));

        Asset asset=new Asset(event.getBytes(),name);
        return repository.save(asset);
    }

    public void delete(Asset asset) {
        repository.delete(asset);
    }
}
