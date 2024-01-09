package com.acetaxi.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PutExchange;

import com.acetaxi.document.model.Response;
import com.acetaxi.document.service.DocumentServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/acetaxi")
@Slf4j
@CrossOrigin("*")
public class DocumentController {

	@Autowired
	private DocumentServiceImpl documentServiceImpl;

	@PostMapping("/driver/uploadDocument")
	public ResponseEntity<Response> uploadFile(@RequestParam(value = "file") MultipartFile file,
			@RequestParam String email,@RequestParam String category) {
		return new ResponseEntity<>(documentServiceImpl.uploadFile(file, email,category), HttpStatus.OK);
	}

	@PutMapping("/driver/updateDocument")
	public ResponseEntity<Response> updateDocument(@RequestParam(value = "file") MultipartFile file,
			@RequestParam String email,@RequestParam String category) {
		return new ResponseEntity<>(documentServiceImpl.uploadFile(file, email,category), HttpStatus.OK);
	}

	@GetMapping("/admin/downloadDocument")
	public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String email, @RequestParam String fileName) {
		byte[] data = documentServiceImpl.downloadFile(email, fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"").body(resource);
	}

	@DeleteMapping("/admin/deleteDocument")
	public ResponseEntity<String> deleteFile(@RequestParam String email, @RequestParam String fileName) {
		log.info("deleteFile(-)");
		return new ResponseEntity<>(documentServiceImpl.deleteFile(email, fileName), HttpStatus.OK);
	}

}
