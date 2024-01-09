package com.acetaxi.document.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.acetaxi.document.model.Response;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

	@Value("${application.bucket.name}")
	private String bucketName;

	@Autowired
	private AmazonS3 s3Client;

	public Response uploadFile(MultipartFile file, String email,String category) {
		Response resp=new Response();
		log.info("uploadFile(-)");
		File fileObj = convertMultiPartFileToFile(file);
		// Generate a folder path with today's date
//		String folderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

		// Create the full S3 object key with the folder path and filename
//		String fileName = folderName + "/" + email + "/" + file.getOriginalFilename();
//		String fileName = email + "/" + file.getOriginalFilename();
		String fileName = email + "/" +category;
		log.info("File Name :"+fileName);

//        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
		fileObj.delete();
		log.info("File uploaded : " + fileName);
		resp.setStatus("success");
		return resp;
	}

	public byte[] downloadFile(String email, String fileName) {
		log.info("downloadFile(-)");
		S3Object s3Object = s3Client.getObject(bucketName, email + "/" + fileName);
		S3ObjectInputStream inputStream = s3Object.getObjectContent();
		try {
			byte[] content = IOUtils.toByteArray(inputStream);
			return content;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	public String deleteFile(String email, String fileName) {
		log.info("deleteFile(-)");
		try {
			s3Client.deleteObject(bucketName, email + "/" + fileName);
			return fileName + " removed ...";
		} catch (AmazonServiceException e) {
			// Handle service-related errors
			// Log the error for debugging
			log.error("Error deleting the file: " + e.getMessage(), e);
			return "Error deleting the file: " + e.getMessage();
		} catch (AmazonClientException e) {
			// Handle client-related errors
			// Log the error for debugging
			log.error("Error deleting the file: " + e.getMessage(), e);
			return "Error deleting the file: " + e.getMessage();
		}
	}

	private File convertMultiPartFileToFile(MultipartFile file) {
		log.info("convertMultiPartFileToFile(-)");
		File convertedFile = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch (IOException e) {
			log.error("Error converting multipartFile to file", e);
		}
		return convertedFile;
	}

}
