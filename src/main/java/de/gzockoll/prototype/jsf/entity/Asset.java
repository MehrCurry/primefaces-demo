package de.gzockoll.prototype.jsf.entity;

import com.google.common.io.ByteStreams;
import com.sun.istack.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.tika.Tika;

import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "data")
@Getter
public class Asset extends AbstractEntity {
    private static final Tika TIKA = new Tika();

    @NotNull
    private String mimeType;
    @NotNull
    @Lob
    private byte[] data;

    private String filename;

    public Asset() {
    }

    public Asset(byte[] data, String filename) {
        this.data = data;
        this.mimeType = TIKA.detect(data);
        this.filename = filename;
    }

    public Asset(InputStream is, String filename) {
        try {
            this.data = ByteStreams.toByteArray(is);
            this.mimeType = TIKA.detect(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Asset(String path) {
        final Path aPath = Paths.get(path);
        createFromPath(aPath);
    }

    private void createFromPath(Path aPath) {
        try {
            this.data = Files.readAllBytes(aPath);
            this.mimeType = TIKA.detect(data);
            this.filename = aPath.getFileName().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Asset(File input) {
        createFromPath(input.toPath());
    }

    public long getSize() {
        return data != null ? data.length : 0L;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public InputStream asByteStream() {
        return new ByteArrayInputStream(data);
    }

    public long sizeInKB() {
        return getSize()/1024L;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
