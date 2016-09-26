package ximg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@EnableConfigurationProperties(xImgProperties.class)
public class xImgService {
    static final Log LOG = LogFactory.getLog(xImgService.class);

    @Autowired
    private xImgProperties properties;

    private String base;

    @PostConstruct
    private void init() {
        base = properties.getBase();
        File dir = new File(base);
        if (!dir.exists()) {
            dir.mkdirs();
            LOG.info("Base dir created, dir=" + dir);
        } else if (dir.isDirectory()) {
            LOG.info("Base dir already exists, dir=" + dir);
        } else {
            LOG.error("Base dir is a, dir=" + dir);
            throw new IllegalStateException();
        }
    }

    public String upload(MultipartFile file) {
        String id = null;
        String path = null;
        try {
            id = xImgUtils.toId(file.getBytes());
            path = xImgUtils.toPath(id);
            File dir = new File(base + path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(base + path + id));
        } catch (IOException e) {
            LOG.error("File uploading failed.", e);
            throw new IllegalStateException(e);
        }
        LOG.info("File uploaded, id=" + id + ", target=" + file.getOriginalFilename());
        return id;
    }

    public byte[] get(String id) {
        String path = xImgUtils.toPath(id);
        File target = new File(base + path + id);
        if (!target.exists()) {
            throw new IllegalArgumentException("File does not exist, target=" + target);
        }
        try {
            byte[] bytes = Files.readAllBytes(target.toPath());
            return bytes;
        } catch (IOException e) {
            LOG.error("File IO error.", e);
            throw new IllegalStateException(e);
        }
    }

}
