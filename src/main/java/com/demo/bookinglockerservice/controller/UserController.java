package com.demo.bookinglockerservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bookinglockerservice.common.CommonLibrary;
import com.demo.bookinglockerservice.dto.UserRequest;
import com.demo.bookinglockerservice.dto.UserResponse;
import com.demo.bookinglockerservice.response.Response;
import com.demo.bookinglockerservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;
	
	@PostMapping("/create")
	public Response createdUser(@RequestBody UserRequest userRequest) {
		Response resp = new Response();
		
		try {
			UserResponse data = userService.createdUser(userRequest);
			
			if (data != null) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
				resp.setData(data);
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
	
	@GetMapping("/get-all")
	public Response getAllUser() {
		Response resp = new Response();
		
		try {
			List<UserResponse> datas = userService.getAllUser();
			
			if (datas.size() > 0) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
				resp.setData(datas);
			} else {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.NOT_FOUND_MESSAGE);
			}
		} catch (Exception e) {
			log.error("Error : ", e);
			
			resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
			resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE);
		}
		
		return resp;
	}
	
	@PostMapping("/update/{id}")
	public Response updatedUser(@PathVariable("id") int id, @RequestBody UserRequest userRequest) {
		Response resp = new Response();
		
		try {
			UserResponse data = userService.updatedUser(id, userRequest);
			
			if (data != null) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
				resp.setData(data);
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
	
	@DeleteMapping("/delete/{id}")
	public Response deletedUser(@PathVariable("id") int id) {
		Response resp = new Response();
		
		try {
			boolean isDeleted = userService.deletedUser(id);
			
			if (isDeleted) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
			} else {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.NOT_FOUND_MESSAGE);
			}
		} catch (Exception e) {
			log.error("Error : ", e);
			
			resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
			resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE);
		}
		
		return resp;
	}
}
