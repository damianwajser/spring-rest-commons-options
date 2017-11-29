package com.github.damianwajser.utils;

import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
@RestController
public class ExceptionHandlerForJunit {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public @ResponseBody ErrorInfo handlerResourceNotFoundException(ResourceNotFoundException ex) {
		return new ErrorInfo(String.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
	}
}
