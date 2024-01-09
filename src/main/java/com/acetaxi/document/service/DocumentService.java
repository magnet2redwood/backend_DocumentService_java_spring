package com.acetaxi.document.service;

import org.springframework.web.multipart.MultipartFile;

import com.acetaxi.document.model.Response;

public interface DocumentService {

	public Response uploadFile(MultipartFile file, String email, String category) ;
}
