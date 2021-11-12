package com.pormarweb.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pormarweb.ecommerce.controller.ProductoController;

@Service
public class UploadFileService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	private String folder = "images//";
	
	public String saveImagen(MultipartFile file) throws IOException {
		
		if (!file.isEmpty()) {
			byte [] bytes=file.getBytes();
			Path path = Paths.get(folder+file.getOriginalFilename());
			LOGGER.info("Este es el contenido del objeto path en saveImagen {}", path);

			Files.write(path, bytes);
			return file.getOriginalFilename();
		}
		return "default.jpg";
		
	}

	public void deleteImagen(String nombre) {
		File file=new File(folder+nombre);
		file.delete();
	}
	
}