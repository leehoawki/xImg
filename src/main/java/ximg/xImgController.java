package ximg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<byte[]> view(@PathVariable String id) {
        byte[] bytes = service.get(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }
}
