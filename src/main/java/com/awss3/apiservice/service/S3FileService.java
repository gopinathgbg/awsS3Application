package com.awss3.apiservice.service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class S3FileService {

	@Value("${s3.bucket.name}")
    private String s3BucketName;
    private final String DOWNLOAD_PATH = "G:\\Bucketfiles\\downloadfiles";
    private final String UPLOAD_PATH = "G:\\Bucketfiles\\uploadfiles";

    @Autowired
    private AmazonS3 amazonS3;

    // List all objects in the bucket
    public List<String> listAllObjects() {

    	System.out.println("Inside list AllObject method");
        List<String> listOfObjects = new ArrayList<>();

        ObjectListing objectListing = amazonS3.listObjects(s3BucketName);
        System.out.println("Object of the bucket "+ objectListing.toString() );
        
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            listOfObjects.add("FileName: " + objectSummary.getKey() +
                    " | " + "LastModified: " + objectSummary.getLastModified() +
                    " | " + "Size: " + objectSummary.getSize());
        }

        return listOfObjects;
    }

    // Downloading object from the bucket
    public String downloadObject(String s3BucketName, String objectName) {
        S3Object s3Object = amazonS3.getObject(s3BucketName, objectName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File( DOWNLOAD_PATH + objectName));
            return DOWNLOAD_PATH + objectName +  " Downloaded Successfully!";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Upload object to the bucket
    public PutObjectResult uploadObject(String s3BucketName, String objectName, File objectToUpload) {
        return amazonS3.putObject(s3BucketName, objectName, objectToUpload);
    }

    public void deleteObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

	public String getS3BucketName() {	
		return this.s3BucketName;
	}

	public String getUPLOAD_PATH() {
		return UPLOAD_PATH;
	}
}