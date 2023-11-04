package com.sistema.trailers.service;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface AlmacenService {
	public void initAlmacenArchivos();
	public String almacenarArchivo(MultipartFile archivo);
	public Path getArchivo(String nameArchivo);
	public Resource getRecurso(String nameArchivo);
	public void deleteArchivo(String nameArchivo);
}
