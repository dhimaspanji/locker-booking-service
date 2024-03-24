package com.demo.bookinglockerservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demo.bookinglockerservice.dto.LoginRequest;
import com.demo.bookinglockerservice.model.Booking;
import com.demo.bookinglockerservice.model.BookingDetail;
import com.demo.bookinglockerservice.repository.BookingDetailRepository;
import com.demo.bookinglockerservice.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	private final BookingRepository bookingRepository;
	private final BookingDetailRepository bookingDetailRepository;
	
	public int loginLocker(LoginRequest loginRequest) {
		Optional<Booking> booking = bookingRepository.findBookingActive(loginRequest.getUserId());
		
		if (booking.isPresent()) {
			List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.get().getBookingId());
			
			if (bookingDetails.size() > 0) {
				boolean isLocker = false;
				
				for (BookingDetail bookingDetail : bookingDetails) {
					if (loginRequest.getLockerId() == bookingDetail.getLockerId()) {
						isLocker = true;
					}
				}
				
				if (isLocker) {
					if (booking.get().getPassword().equals(loginRequest.getPassword())) {
						if (booking.get().getInUse() == 2) {
							return 3;
						} else {
							int inUse = booking.get().getInUse() + 1;
							int isUpdateInUse = bookingRepository.updateInUse(booking.get().getBookingId(), inUse);
							
							if (isUpdateInUse > 0) {								
								return 1;
							} else {
								return 4;
							}
						}
					} else {
						if (booking.get().getInvalidPasswordAttempts() == 3) {
							return 5;
						} else {
							int invalidPasswordAttempts = booking.get().getInvalidPasswordAttempts() + 1;
							int isUpdateInvalidPasswordAttempts = bookingRepository.updateInvalidPasswordAttempts(booking.get().getBookingId(), invalidPasswordAttempts);
							
							if (isUpdateInvalidPasswordAttempts > 0) {
								return 6;							
							} else {
								return 4;
							}
						}
					}
				} else {
					return 2;
				}
			}
			
			return 2;
		}
		
		return 0;
	}
}
