package ximg;

import org.apache.catalina.WebResource;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.coyote.http2.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.LRUCache;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
public class xImgController {
    static final Log LOG = LogFactory.getLog(xImgController.class);

    @Autowired
    xImgService service;

    @RequestMapping("/")
    public String index() {
        return "xImg server started!";
    }

    @RequestMapping(value = "/upload")
    @ResponseBody
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String id = service.upload(file);
        Map<String, String> response = new HashMap<String, String>();
        response.put("id", id);
        response.put("name", file.getOriginalFilename());
        return response;
    }

    @RequestMapping(value = "/view/{id}")
    public void view(@PathVariable String id,
                     @RequestParam(required = false, defaultValue = "0") int width,
                     @RequestParam(required = false, defaultValue = "0") int height,
                     HttpServletResponse resp) throws IOException {
        File file = service.get(id, width, height);
        resp.setHeader("Content-Type", "image/jpeg");
        try (FileInputStream fis = new FileInputStream(file)) {
            copy(fis, resp.getOutputStream());
        }
    }

    @RequestMapping(value = "/exists/{id}")
    public boolean exists(@PathVariable String id) {
        return service.exists(id);
    }

    void copy(InputStream istream, ServletOutputStream ostream) throws IOException {
        byte buffer[] = new byte[2048];
        int len = istream.read(buffer);
        while (len > 0) {
            ostream.write(buffer, 0, len);
            len = istream.read(buffer);
        }
    }
}
