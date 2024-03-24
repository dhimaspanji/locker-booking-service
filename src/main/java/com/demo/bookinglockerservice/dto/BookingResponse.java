package com.demo.bookinglockerservice.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

	private int bookingId;
	private int userId;
	private double deposit;
	private double denda;
	private int inUse;
	private String status;
	private String password;
	private int invalidPasswordAttempts;
	private double costUnlockLocker;
	private String note;
	private Date startDate;
	private Date endDate;
	private List<BookingDetailResponse> bookingDetail;
}
