package com.despani.x2.core.services;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonGenericService {

    private AmazonS3 s3client;

    BasicAWSCredentials credentials = null;

    @Value("${amazon.s3.accessKey}")
    private String accessKey;

    @Value("${amazon.s3.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }


    public String getS3ObjectUrl(String bucketName, String fileName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }


    private void uploadFileTos3bucket(String fileName, File file, String bucketName) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }


    public String uploadFile(String fileName, String contenttype, String bucketNameParam, InputStream is) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contenttype);
        metadata.setContentLength(is.available());
        metadata.addUserMetadata("x-amz-meta-title", "user tresponded");
        PutObjectRequest request = new PutObjectRequest(bucketNameParam, fileName, is, metadata);
        request.setMetadata(metadata);
        s3client.putObject(request);
        return getS3ObjectUrl(bucketNameParam, fileName);
    }


    public void deleteFile(String bucket, String fileName) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        s3client.deleteObject(deleteObjectRequest);
    }


}
