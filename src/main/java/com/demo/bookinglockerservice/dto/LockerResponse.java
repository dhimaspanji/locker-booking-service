package com.demo.bookinglockerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockerResponse {

	private int lockerId;
	private int noLocker;
	private int inUse;
}
