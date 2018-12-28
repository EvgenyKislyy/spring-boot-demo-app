package com.demoapp.error;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ ResourceNotFoundException.class, NoSuchElementException.class,
			EmptyResultDataAccessException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ApiError handleNotFound(Exception exception, HttpServletRequest request) {

		return new ApiError(HttpStatus.NOT_FOUND, exception.getMessage(), exception.getClass().getSimpleName());
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class,
			MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
			MissingServletRequestParameterException.class, DataIntegrityViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ApiError handleHttpBadRequest(Exception exception, HttpServletRequest request) {

		return new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage(), exception.getClass().getSimpleName());
	}

}
