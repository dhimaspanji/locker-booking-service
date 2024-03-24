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
import com.demo.bookinglockerservice.dto.LockerRequest;
import com.demo.bookinglockerservice.dto.LockerResponse;
import com.demo.bookinglockerservice.response.Response;
import com.demo.bookinglockerservice.service.LockerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/locker")
@RequiredArgsConstructor
@Slf4j
public class LockerController {

	private final LockerService lockerService;
	
	@PostMapping("/create")
	public Response createdLocker(@RequestBody LockerRequest lockerRequest) {
		Response resp = new Response();
		
		try {
			LockerResponse data = lockerService.createdLocker(lockerRequest);
			
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
	public Response getAllLocker() {
		Response resp = new Response();
		
		try {
			List<LockerResponse> datas = lockerService.getAllLocker();
			
			if (datas.size() > 0) {
				resp.setCode(CommonLibrary.OK_CODE);
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
	public Response updatedLocker(@PathVariable("id") int id, @RequestBody LockerRequest lockerRequest) {
		Response resp = new Response();
		
		try {
			LockerResponse data = lockerService.updatedLocker(id, lockerRequest);
			
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
	
	@PostMapping("/update-inUse/{id}/{inUse}")
	public Response updatedInUseLocker(@PathVariable("id") int id, @PathVariable("inUse") int inUse) {
		Response resp = new Response();
		
		try {
			LockerResponse data = lockerService.updatedInUseLocker(id, inUse);
			
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
	public Response deletedLocker(@PathVariable("id") int id) {
		Response resp = new Response();
		
		try {
			boolean isDeleted = lockerService.deletedLocker(id);
			
			if (isDeleted) {
				resp.setCode(CommonLibrary.OK_CODE);
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
	
	@PostMapping("/exemption/{id}")
	public Response lockerExemption(@PathVariable("id") int bookingId) {
		Response resp = new Response();
		
		try {
			int isLockerExemption = lockerService.lockerExemption(bookingId);
			
			if (isLockerExemption == 1) {
				resp.setCode(CommonLibrary.OK_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
			} else if (isLockerExemption == 2) {
				resp.setCode(CommonLibrary.INTERNAL_SERVER_ERROR_CODE);
				resp.setMessage(CommonLibrary.INTERNAL_SERVER_ERROR_MESSAGE);
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
