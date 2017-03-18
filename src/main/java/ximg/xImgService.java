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
import java.util.Map;

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
        File dir = null;
        try {
            id = xImgUtils.toId(file.getBytes());
            path = xImgUtils.toPath(id);
            dir = new File(base + path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.transferTo(new File(base + path + id));
        } catch (IOException e) {
            LOG.error("File uploading failed.", e);
            if (dir != null) dir.delete();
            throw new IllegalStateException(e);
        }
        LOG.info("File uploaded, id=" + id + ", target=" + file.getOriginalFilename());
        return id;
    }

    public byte[] get(String id, int width, int height) {
        String target = base + xImgUtils.toPath(id) + id;
        File img = new File(target);

        if (!img.exists()) {
            throw new IllegalArgumentException("File does not exist, target=" + target);
        }
        if (width < 0) width = 0;
        if (height < 0) height = 0;
        if (width > 0 || height > 0) {
            img = new File(target + "_" + width + "_" + height);
            if (!img.exists()) {
                xImgUtils.resize(target, img.getAbsolutePath(), width, height);
            }
        }
        try {
            byte[] bytes = Files.readAllBytes(img.toPath());
            return bytes;
        } catch (IOException e) {
            LOG.error("File IO error.", e);
            throw new IllegalStateException(e);
        }
    }

    public boolean exists(String id) {
        if (id.length() != 32) {
            throw new IllegalArgumentException("Illegal md5 value, id=" + id);
        }
        String path = xImgUtils.toPath(id);
        return new File(base + path).exists();
    }
}
