package com.demo.bookinglockerservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demo.bookinglockerservice.dto.LockerRequest;
import com.demo.bookinglockerservice.dto.LockerResponse;
import com.demo.bookinglockerservice.model.Booking;
import com.demo.bookinglockerservice.model.Locker;
import com.demo.bookinglockerservice.repository.BookingRepository;
import com.demo.bookinglockerservice.repository.LockerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LockerService {

	private final BookingRepository bookingRepository;
	private final LockerRepository lockerRepository;
	
	public LockerResponse createdLocker(LockerRequest lockerRequest) {
		Locker locker = new Locker();
		locker.setNoLocker(lockerRequest.getNoLocker());
		locker.setInUse(lockerRequest.getInUse());
		locker.setCreatedAt(new Date());
		
		lockerRepository.save(locker);
		log.info("Locker {} is saved", locker.getLockerId());
		
		Optional<Locker> data = lockerRepository.findById(locker.getLockerId());
		
		if (data.isPresent()) {
			return LockerResponse.builder()
					.lockerId(locker.getLockerId())
					.noLocker(lockerRequest.getNoLocker())
					.inUse(lockerRequest.getInUse())
					.build();
		}
		
		return null;
	}
	
	public List<LockerResponse> getAllLocker() {
		List<Locker> lockers = lockerRepository.findAll();
		
		return lockers.stream().map(this::mapToLockerResponse).toList();
	}
	
	public LockerResponse updatedLocker(int id, LockerRequest lockerRequest) {
		Optional<Locker> data = lockerRepository.findById(id);
		
		if (data.isPresent()) {
			Locker locker = new Locker();
			locker.setLockerId(id);
			locker.setNoLocker(lockerRequest.getNoLocker());
			locker.setInUse(lockerRequest.getInUse());
			locker.setCreatedAt(data.get().getCreatedAt());
			locker.setUpdatedAt(new Date());
			
			lockerRepository.save(locker);
			log.info("Locker {} is updated", id);
			
			return LockerResponse.builder()
					.lockerId(locker.getLockerId())
					.noLocker(lockerRequest.getNoLocker())
					.inUse(lockerRequest.getInUse())
					.build();
		}
		
		return null;
	}
	
	public LockerResponse updatedInUseLocker(int id, int inUse) {
		Optional<Locker> data = lockerRepository.findById(id);
		
		if (data.isPresent()) {
			int isUpdateInUse = lockerRepository.updateInUseLocker(id, inUse);
			
			if (isUpdateInUse > 0) {				
				return LockerResponse.builder()
						.lockerId(id)
						.noLocker(data.get().getNoLocker())
						.inUse(1)
						.build();
			}
		}
		
		return null;
	}
	
	public boolean deletedLocker(int id) {
		Optional<Locker> locker = lockerRepository.findById(id);
		
		if (locker.isPresent()) {
			lockerRepository.deleteById(id);
			log.info("Locker {} is deleted", id);
			
			return true;
		}
		
		return false;
	}
	
	public int lockerExemption(int bookingId) {
		Optional<Booking> booking = bookingRepository.findById(bookingId);
		
		if (booking.isPresent()) {
			double costUnlockLocker = booking.get().getCostUnlockLocker() + 25000;
			int isUpdateCostUnlockLocker = bookingRepository.updateCostUnlockLocker(bookingId, costUnlockLocker);
			
			if (isUpdateCostUnlockLocker > 0) {
				return 1;
			} else {
				return 2;
			}
		}
		
		return 0;
	}
	
	private LockerResponse mapToLockerResponse(Locker locker) {
		return LockerResponse.builder()
				.lockerId(locker.getLockerId())
				.noLocker(locker.getNoLocker())
				.inUse(locker.getInUse())
				.build();
	}
}
