package com.demo.bookinglockerservice.common;

import org.springframework.http.HttpStatus;

public class CommonLibrary {

	// Response Code
	public static final int OK_CODE = HttpStatus.OK.value();
	public static final int CONFLICT_CODE = HttpStatus.CONFLICT.value();
	public static final int CREATED_CODE = HttpStatus.CREATED.value();
	public static final int NOT_FOUND_CODE = HttpStatus.NOT_FOUND.value();
	public static final int BAD_REQUEST_CODE = HttpStatus.BAD_REQUEST.value();
	public static final int INTERNAL_SERVER_ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	// Response Message
	public static final String SUCCESS_MESSAGE = "Success";
	public static final String FAILED_MESSAGE = "Failed";
	public static final String CONFLICT_MESSAGE = "Conflict";
	public static final String NOT_FOUND_MESSAGE = "Data not found";
	public static final String BAD_REQUEST_MESSAGE = "Bad Request";
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";
	
	public static final String DATA_ALREADY_EXIST = "Data already exist";
	public static final String DUPLICATE_IS_NOT_ALLOWED = "Duplicate is not allowed";
}
