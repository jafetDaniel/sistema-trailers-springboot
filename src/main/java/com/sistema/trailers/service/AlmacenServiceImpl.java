package com.sistema.trailers.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sistema.trailers.exceptions.AlmacenException;
import com.sistema.trailers.exceptions.FileNotFoundException;

import jakarta.annotation.PostConstruct;

@Service
public class AlmacenServiceImpl implements AlmacenService{
	
	@Value("${storage.location}")
	private String storageLocation;

	@PostConstruct //indicar que se va ejecutar cada vez que haya una nueva instancia
	@Override
	public void initAlmacenArchivos() {
		try {
			Files.createDirectories(Paths.get(storageLocation));
		} catch (IOException e) {
			throw new AlmacenException("error al inicializar la ubicación en el almacen de archivos");
		}
	}

	@Override
	public String almacenarArchivo(MultipartFile archivo) {
		String nameArchivo = archivo.getOriginalFilename();
		if (archivo.isEmpty()) {
			throw new AlmacenException("no se puede almacenar un archivo vacío");
		}
		try {
			InputStream inputStream = archivo.getInputStream();
			Files.copy(inputStream, Paths.get(storageLocation).resolve(nameArchivo), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new AlmacenException("error al almacenar el archivo "+nameArchivo, e);
		}
		return nameArchivo;
	}

	@Override
	public Path getArchivo(String nameArchivo) {
		return Paths.get(storageLocation).resolve(nameArchivo);
	}

	@Override
	public Resource getRecurso(String nameArchivo) {
		try {
			Path archivo = getArchivo(nameArchivo);
			Resource recurso = new UrlResource(archivo.toUri());
			
			if (recurso.exists() || recurso.isReadable()) {
				return recurso;
			}else {
				throw new FileNotFoundException("no se pudo encontrar el archivo "+nameArchivo);
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("no se pudo encontrar el archivo "+nameArchivo, e);
		}
	}

	@Override
	public void deleteArchivo(String nameArchivo) {
		Path archivo = getArchivo(nameArchivo);
		try {
			FileSystemUtils.deleteRecursively(archivo);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
