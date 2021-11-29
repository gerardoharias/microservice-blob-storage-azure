package com.quelonios.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quelonios.dto.UploadFileDTO;
import com.quelonios.service.BlobService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Blob  Controller", tags = { "service storage " })
@RequestMapping("/blob")
public class ApiBlobController {
	
	private final Logger log = LoggerFactory.getLogger(ApiBlobController.class);

	@Autowired
	private BlobService myBlobService;

    @GetMapping("/")
    public List<String> blobitemst() {
        return myBlobService.listFiles();
    }


    @GetMapping("/download/{filename}")
    public byte[] download(@PathVariable String filename) {
        log.info("download blobitem: {}", filename);
         return myBlobService.downloadFile(filename).toByteArray();
    }
    
    @GetMapping("/downloadbase64/{filename}")
    public String downloadbase64(@PathVariable String filename) {
        log.info("download blobitem: {}", filename);
         return myBlobService.downloadFileBase64(filename);
    }
    
    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Filename :" + file.getOriginalFilename());
        log.info("Size:" + file.getSize());
        log.info("Contenttype:" + file.getContentType());
        myBlobService.storeFile(file.getOriginalFilename(),file.getInputStream(), file.getSize());
        
        return file.getOriginalFilename() + " Has been saved as a blob-item!!!";

    }
    
    @PostMapping("/uploadbase64")
    public String uploadFileBase64(@RequestBody UploadFileDTO upload) throws IOException {
        log.info("Filename base 64 :" );
        myBlobService.storeFileBase64(upload.getFilename(), upload.getBase64());
        
        return upload.getFilename() + " Has been saved as a blob-item from base64!!!";

    }
}
