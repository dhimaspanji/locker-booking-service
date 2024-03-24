package com.demo.bookinglockerservice.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "t_booking")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private Date createdAt;
}
