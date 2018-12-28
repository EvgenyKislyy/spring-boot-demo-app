package com.demoapp.error;

import org.springframework.http.HttpStatus;

public class ApiError {
	private HttpStatus status;
	private String message;
	private String error;

	public ApiError() {
		super();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public ApiError(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.message = message;
		this.error = error;
	}
}
