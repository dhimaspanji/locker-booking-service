package com.demo.bookinglockerservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bookinglockerservice.common.CommonLibrary;
import com.demo.bookinglockerservice.dto.LoginRequest;
import com.demo.bookinglockerservice.response.Response;
import com.demo.bookinglockerservice.service.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

	private final LoginService loginService;
	
	@PostMapping("/locker")
	public Response loginLocker(@RequestBody LoginRequest loginRequest) {
		Response resp = new Response();
		
		try {
			int isLogin = loginService.loginLocker(loginRequest);
			
			if (isLogin == 1) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
			} else if (isLogin == 2) {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data locker not found");
			} else if (isLogin == 3) {
				resp.setCode(CommonLibrary.BAD_REQUEST_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data locker already in use");
			} else if (isLogin == 4) {
				resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
				resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE + ". Failed to update in_use");
			} else if (isLogin == 5) {
				resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
				resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE + ". Failed to update invalid_password_attempts");
			} else if (isLogin == 6) {
				resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Incorrect password");
			} else {
				resp.setCode(CommonLibrary.BAD_REQUEST_CODE);
				resp.setMessage(CommonLibrary.BAD_REQUEST_MESSAGE);
			}
		} catch (Exception e) {
			log.error("Error : ", e);
			
			resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
			resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE);
		}
		
		return resp;
	}
}
