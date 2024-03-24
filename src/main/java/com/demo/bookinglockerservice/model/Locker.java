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
@Table(name = "t_locker")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Locker {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lockerId;
	private int noLocker;
	private int inUse;
	private Date createdAt;
	private Date updatedAt;
}
