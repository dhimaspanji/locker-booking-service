package com.demo.bookinglockerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.bookinglockerservice.model.BookingDetail;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {

	@Query(value = "SELECT * FROM t_booking_detail WHERE booking_id = :bookingId",
			nativeQuery = true)
	List<BookingDetail> findByBookingId(@Param("bookingId") int bookingId);
}
