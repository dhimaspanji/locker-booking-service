package com.demo.bookinglockerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.bookinglockerservice.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
