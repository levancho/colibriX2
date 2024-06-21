package com.despani.x2.core.services;

import com.despani.x2.core.beans.enums.DespMediaTypes;
import com.despani.x2.core.exceptions.base.DespRuntimeException;
import com.despani.x2.core.xmedia.beans.domains.DespMedia;
import com.despani.x2.core.xmedia.services.DespMediaService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class AWSFileManagementService {

    @Autowired
    DespMediaService despMediaService;

    @Autowired
    AmazonGenericService aws;


    public DespMedia validateAndCreateDespMedia(InputStream fileStream, String originalFileName, String option, int oid) throws IOException {
        DespMedia media = new DespMedia();
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        Tika tika = new Tika();
        String format = tika.detect(fileStream);
        String filename = option + "-"+oid+"-"+randomUUIDString+"-"+originalFileName.substring(0, originalFileName.indexOf(".")+1)+format.substring(format.indexOf("/")+1);
        String description = originalFileName.substring(0, originalFileName.indexOf(".")+1)+format.substring(format.indexOf("/")+1);
        DespMediaTypes t = DespMediaTypes.get(format);
        if (t != null) {
            media.setFilename(filename);
            media.setDescription(description);
            media.setType(t.getCode());
            media.setPrimary(false);
            media.setUrl("https://zaconne.s3.amazonaws.com/" + filename);
            media.setFormat(t.getValue());
        } else {
            throw new DespRuntimeException("invalid file type " + format);
        }
        return media;
    }






    public DespMedia generateMedia(InputStream input, String filename, int oid, String option) throws IOException {
        DespMedia media = validateAndCreateDespMedia(input, filename, option, oid);
        aws.uploadFile(media.getFilename(), media.getFormat(), "zaconne", input);
        despMediaService.createDespMedia(media);
        return media;
    }


    public void deleteFileFromAWS(String bucket, String fileName) {
        aws.deleteFile(bucket, fileName);
    }
}
