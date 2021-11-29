package com.quelonios.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import org.apache.commons.codec.binary.Base64;


@Service
public class BlobService {

	private final Logger log = LoggerFactory.getLogger(BlobService.class);
	
	@Autowired
	private AzureBlobProperties azureBlobProperties;

    private BlobContainerClient containerClient() {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(azureBlobProperties.getConnectionstring()).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(azureBlobProperties.getContainer());
        return container;
    }

    public List<String> listFiles() {
        log.info("List blobs BEGIN");
        BlobContainerClient container = containerClient();
        ArrayList<String> list = new ArrayList<String>();
        for (BlobItem blobItem : container.listBlobs()) {
            log.info("Blob {}", blobItem.getName());
            list.add(blobItem.getName());
        }
        log.info("List blobs END");
        return list;
    }

    public ByteArrayOutputStream downloadFile(String blobitem) {
        log.info("Download BEGIN {}", blobitem);
        BlobContainerClient containerClient = containerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobitem);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blobClient.download(os);
        log.info("Download END");     
        
        return os;
    }
    
    public String downloadFileBase64(String blobitem) {
        log.info("Download BEGIN {}", blobitem);
        BlobContainerClient containerClient = containerClient();
        BlobClient blobClient = containerClient.getBlobClient(blobitem);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blobClient.download(os);
        log.info("Download END");
        String encodedBase64 = "";
        encodedBase64 = new String(Base64.encodeBase64(os.toByteArray()));
        log.info("encodeBase64 end");
        return encodedBase64;
    }

    public String storeFile(String filename, InputStream content, long length) {
        log.info("Azure store file BEGIN {}", filename);
        BlobClient client = containerClient().getBlobClient(filename);
        if (client.exists()) {
            log.warn("The file was already located on azure");
        } else {
            client.upload(content, length);
        }

        log.info("Azure store file END");
        return "File uploaded with success!";
    }
    
    public String storeFileBase64( String filename, String base64 ) {
        log.info("Azure store file base64 BEGIN {}");
        BlobClient client = containerClient().getBlobClient(filename);
      //  String sendFileName = filename.replaceAll(" ", "");
        if (client.exists()) {
            log.warn("The file was already located on azure");
        } else {
        	byte[] bytes = Base64.decodeBase64(base64.getBytes());
        	
        	try (ByteArrayInputStream dataStream = new ByteArrayInputStream(bytes)) {
        		client.upload(dataStream, bytes.length);
        	} catch (IOException e) {
        	    e.printStackTrace();
        	}
        	//InputStream is = new FileInputStream(sendFileName);
        	//FileCopyUtils(file, (InputStream) is);
        	
            //client.upload(sdsd, length);
        }

        log.info("Azure store file END");
        return "File uploaded with success!";
    }
 
}
