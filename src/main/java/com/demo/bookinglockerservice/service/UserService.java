package com.demo.bookinglockerservice.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.demo.bookinglockerservice.dto.UserRequest;
import com.demo.bookinglockerservice.dto.UserResponse;
import com.demo.bookinglockerservice.model.User;
import com.demo.bookinglockerservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	
	public UserResponse createdUser(UserRequest userRequest) {
		User user = new User();
		user.setNoHp(userRequest.getNoHp());
		user.setNoKtp(userRequest.getNoKtp());
		user.setEmail(userRequest.getEmail());
		user.setCreatedAt(new Date());
		
		userRepository.save(user);
		log.info("User {} is saved", user.getUserId());
		
		Optional<User> data = userRepository.findById(user.getUserId());
		
		if (data.isPresent()) {
			return UserResponse.builder()
					.userId(user.getUserId())
					.noHp(userRequest.getNoHp())
					.noKtp(userRequest.getNoKtp())
					.email(userRequest.getEmail())
					.build();
		}
		
		return null;
	}
	
	public List<UserResponse> getAllUser(){
		List<User> users = userRepository.findAll();
		
		return users.stream().map(this::mapToUserResponse).toList();
	}
	
	public UserResponse updatedUser(int id, UserRequest userRequest) {
		Optional<User> data = userRepository.findById(id);
		
		if (data.isPresent()) {
			User user = new User();
			user.setUserId(id);
			user.setNoHp(userRequest.getNoHp());
			user.setNoKtp(userRequest.getNoKtp());
			user.setEmail(userRequest.getEmail());
			user.setCreatedAt(data.get().getCreatedAt());
			user.setUpdatedAt(new Date());
			
			userRepository.save(user);
			log.info("User {} is updated", id);
			
			return UserResponse.builder()
					.userId(id)
					.noHp(userRequest.getNoHp())
					.noKtp(userRequest.getNoKtp())
					.email(userRequest.getEmail())
					.build();
		}
		
		return null;
	}
	
	public boolean deletedUser(int id) {
		Optional<User> user = userRepository.findById(id);
		
		if (user.isPresent()) {
			userRepository.deleteById(id);
			log.info("User {} is deleted", id);
			
			return true;
		}
		
		return false;
	}
	
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.noHp(user.getNoHp())
				.noKtp(user.getNoKtp())
				.email(user.getEmail())
				.build();
	}
}
