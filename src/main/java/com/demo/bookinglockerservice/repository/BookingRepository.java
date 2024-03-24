package com.demo.bookinglockerservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.bookinglockerservice.model.Booking;

import jakarta.transaction.Transactional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

	@Query(value = "SELECT * FROM t_booking WHERE user_id = :userId AND status = 'active'",
			nativeQuery = true)
	Optional<Booking> findBookingActive(@Param("userId") int userId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_booking SET in_use = :inUse WHERE booking_id = :bookingId",
			nativeQuery = true)
	int updateInUse(@Param("bookingId") int bookingId, @Param("inUse") int inUse);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_booking SET invalid_password_attempts = :invalidPasswordAttempts WHERE booking_id = :bookingId",
			nativeQuery = true)
	int updateInvalidPasswordAttempts(@Param("bookingId") int bookingId, @Param("invalidPasswordAttempts") int invalidPasswordAttempts);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_booking SET cost_unlock_locker = :costUnlockLocker WHERE booking_id = :bookingId",
			nativeQuery = true)
	int updateCostUnlockLocker(@Param("bookingId") int bookingId, @Param("costUnlockLocker") double costUnlockLocker);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE t_booking SET denda = :denda, status = 'inactive', start_date = null, end_date = null, note = :note WHERE booking_id = :bookingId",
			nativeQuery = true)
	int updateBooking(@Param("bookingId") int bookingId, @Param("denda") double denda, @Param("note") String note);
}
