package de.gzockoll.prototype.jsf.boundary.rest;

import com.google.common.base.Preconditions;
import de.gzockoll.prototype.jsf.entity.Asset;
import de.gzockoll.prototype.jsf.entity.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@Transactional
public class AssetController {

    @Autowired
    private AssetRepository repository;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    Asset handleFileUpload(
            @RequestParam(value="file", required=true) MultipartFile file) {

        Asset asset = null;
        try {
            int sizeUpload = file.getBytes().length;
            asset = new Asset(file.getInputStream(),file.getOriginalFilename());
            int sizeAsset=asset.getData().length;
            Preconditions.checkState(sizeUpload==sizeAsset);
            repository.save(asset);
        } catch (IOException e) {
            log.error("Error: " + e);
        }
        return asset;
    }

    @RequestMapping(value = "/assets/{id}", method = RequestMethod.GET)
    public HttpEntity<Asset> findDocument(@PathVariable(value = "id") Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        final Asset asset = repository.findOne(id);
        return new ResponseEntity<>(asset, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/assets/raw/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> sendDocument(@PathVariable(value = "id") Long id) {
        final Asset asset = repository.findOne(id);
        long size=asset.getData().length;
        log.debug("Size " + size);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(asset.getMimeType()));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(asset.getData(), headers, HttpStatus.OK);
        return response;
    }

    public void save(Asset a) {
        Preconditions.checkNotNull(a);
        repository.save(a);
    }

    public void fileImport(Exchange ex) {
        File file= (File) ex.getIn().getBody(GenericFile.class).getFile();
        repository.save(new Asset(file));
    }
}
