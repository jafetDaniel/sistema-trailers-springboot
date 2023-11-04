package com.sistema.trailers.exceptions;

public class AlmacenException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AlmacenException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlmacenException(String message) {
		super(message);
	}

}
