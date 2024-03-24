package com.demo.bookinglockerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.bookinglockerservice.model.Locker;

import jakarta.transaction.Transactional;

public interface LockerRepository extends JpaRepository<Locker, Integer> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE t_locker SET in_use = :inUse WHERE locker_id = :lockerId",
			nativeQuery = true)
	int updateInUseLocker(@Param("lockerId") int lockerId, @Param("inUse") int inUse);
}
