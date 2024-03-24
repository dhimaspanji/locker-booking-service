package com.demo.bookinglockerservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.bookinglockerservice.common.CommonLibrary;
import com.demo.bookinglockerservice.dto.BookingRequest;
import com.demo.bookinglockerservice.dto.BookingResponse;
import com.demo.bookinglockerservice.response.Response;
import com.demo.bookinglockerservice.service.BookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

	private final BookingService bookingService;
	
	@PostMapping("/create")
	public Response createdBooking(@RequestBody BookingRequest bookingRequest) {
		Response resp = new Response();
		
		try {
			int isCreated = bookingService.createdBooking(bookingRequest);
			
			if (isCreated == 1) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
			} else if (isCreated == 2) {
				resp.setCode(CommonLibrary.BAD_REQUEST_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Maximum lockers only 3");
			} else if (isCreated == 3) {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data user not found");
			} else if (isCreated == 4) {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data locker not found");
			} else if (isCreated == 5) {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data locker in used");
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
	
	@PostMapping("/return/{id}")
	public Response returnBooking(@PathVariable("id") int id) {
		Response resp = new Response();
		
		try {
			int isReturn = bookingService.returnBooking(id);
			
			if (isReturn == 1) {
				resp.setCode(CommonLibrary.CREATED_CODE);
				resp.setMessage(CommonLibrary.SUCCESS_MESSAGE);
			} else if (isReturn == 2) {
				resp.setCode(CommonLibrary.NOT_FOUND_CODE);
				resp.setMessage(CommonLibrary.FAILED_MESSAGE + ". Data booking is inactive");
			} else if (isReturn == 3) {
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
	
	@GetMapping("/get-all")
	public Response getAllBooking() {
		Response resp = new Response();
		
		try {
			List<BookingResponse> datas = bookingService.getAllBooking();
			
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
}
