package com.demo.bookinglockerservice.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.demo.bookinglockerservice.dto.BookingDetailRequest;
import com.demo.bookinglockerservice.dto.BookingDetailResponse;
import com.demo.bookinglockerservice.dto.BookingRequest;
import com.demo.bookinglockerservice.dto.BookingResponse;
import com.demo.bookinglockerservice.model.Booking;
import com.demo.bookinglockerservice.model.BookingDetail;
import com.demo.bookinglockerservice.model.Locker;
import com.demo.bookinglockerservice.model.User;
import com.demo.bookinglockerservice.repository.BookingDetailRepository;
import com.demo.bookinglockerservice.repository.BookingRepository;
import com.demo.bookinglockerservice.repository.LockerRepository;
import com.demo.bookinglockerservice.repository.UserRepository;
import com.demo.bookinglockerservice.response.Response;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
	
	@Value("${spring.mail.username}")
	private String emailFrom;

	private final JavaMailSender mailSender;
	private final WebClient.Builder webClientBuilder;
	
	private final UserRepository userRepository;
	private final LockerRepository lockerRerpository;	
	private final BookingRepository bookingRepository;
	private final BookingDetailRepository bookingDetailRepository;
	
	public int createdBooking(BookingRequest bookingRequest) {
		String randomCode = RandomStringUtils.randomAlphanumeric(6);
		int totalLocker = bookingRequest.getBookingDetail().size();
		
		if (totalLocker > 3) {
			return 2;
		}
		
		int totalDeposit = totalLocker * 10000;
		
		Optional<User> dataUser = userRepository.findById(bookingRequest.getUserId());
		
		if (!dataUser.isPresent()) {
			return 3;
		}
		
		for (BookingDetailRequest bookingDetailReq : bookingRequest.getBookingDetail()) {
			Optional<Locker> dataLocker = lockerRerpository.findById(bookingDetailReq.getLockerId());
			
			if (!dataLocker.isPresent()) {
				return 4;
			} else {
				if (dataLocker.get().getInUse() > 0) {
					return 5;
				}
			}
		}
		
		Optional<Booking> dataBooking = bookingRepository.findBookingActive(bookingRequest.getUserId());
		
		if (dataBooking.isPresent()) {
			List<BookingDetail> dataBookingDetails = bookingDetailRepository.findByBookingId(dataBooking.get().getBookingId());
			
			if (dataBookingDetails.size() == 3) {
				return 2;
			}
			
			String noLockerStr = "";
			
			for (BookingDetailRequest bookingDetailReq : bookingRequest.getBookingDetail()) {
				for (BookingDetail bookingDetail : dataBookingDetails) {
					if (bookingDetailReq.getLockerId() == bookingDetail.getLockerId()) {
						return 5;
					}
				}
				
				BookingDetail bookingDetail = new BookingDetail();
				bookingDetail.setBookingId(dataBooking.get().getBookingId());
				bookingDetail.setLockerId(bookingDetailReq.getLockerId());
				
				bookingDetailRepository.save(bookingDetail);
				log.info("Booking detail {} is saved");
				
				webClientBuilder.build().post()
						.uri("http://localhost:8080/api/locker/update-inUse/" + bookingDetailReq.getLockerId())
						.retrieve()
						.bodyToMono(Response.class)
						.block();
				
				Optional<Locker> data = lockerRerpository.findById(bookingDetailReq.getLockerId());
				
				noLockerStr = noLockerStr + String.valueOf(data.get().getNoLocker()) + " ";
			}
			
			sendEmail(noLockerStr, dataBooking.get().getPassword(), dataUser.get().getEmail());
			
			return 1;
		} else {
			Date endDate = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(endDate); 
			c.add(Calendar.DATE, 1);
			endDate = c.getTime();
			
			Booking booking = new Booking();
			booking.setUserId(bookingRequest.getUserId());
			booking.setDeposit(totalDeposit);
			booking.setDenda(0);
			booking.setInUse(0);
			booking.setStatus("active");
			booking.setPassword(randomCode);
			booking.setInvalidPasswordAttempts(0);
			booking.setCostUnlockLocker(0);
			booking.setStartDate(new Date());
			booking.setEndDate(endDate);
			booking.setCreatedAt(new Date());
			
			bookingRepository.save(booking);
			log.info("Booking {} is saved", booking.getBookingId());
			
			String noLockerStr = "";
			
			for (BookingDetailRequest bookingDetailReq : bookingRequest.getBookingDetail()) {			
				BookingDetail bookingDetail = new BookingDetail();
				bookingDetail.setBookingId(booking.getBookingId());
				bookingDetail.setLockerId(bookingDetailReq.getLockerId());
				
				bookingDetailRepository.save(bookingDetail);
				log.info("Booking detail {} is saved");
				
				webClientBuilder.build().post()
						.uri("http://localhost:8080/api/locker/update-inUse/" + bookingDetailReq.getLockerId() + "/1")
						.retrieve()
						.bodyToMono(Response.class)
						.block();
				
				Optional<Locker> data = lockerRerpository.findById(bookingDetailReq.getLockerId());
				
				noLockerStr = noLockerStr + String.valueOf(data.get().getNoLocker()) + " ";
			}
			
			sendEmail(noLockerStr, randomCode, dataUser.get().getEmail());
			
			Optional<Booking> data = bookingRepository.findById(booking.getBookingId());
			
			if (data.isPresent()) {
				return 1;
			};
		}
		
		return 0;
	}
	
	public int returnBooking(int id) {
		Optional<Booking> booking = bookingRepository.findById(id);
		
		if (booking.isPresent()) {
			if (booking.get().getStatus().equals("active")) {
				Date dateNow = new Date();
				
				if (dateNow.compareTo(booking.get().getEndDate()) > 0) {
					long diffInMillies = Math.abs(dateNow.getTime() - booking.get().getEndDate().getTime());
					long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
					
					double denda = 5000 * days;
					double totalDenda;
					String note = "";
					
					if (booking.get().getCostUnlockLocker() > 0) {
						totalDenda = denda + booking.get().getCostUnlockLocker();
						note = "Pengembalian locker dengan denda sebesar " + totalDenda;
					} else {

						if (denda > booking.get().getDeposit()) {
							totalDenda = denda - booking.get().getDeposit(); 
							note = "Pengembalian locker dengan denda sebesar " + denda + ". Setelah di potong dengan deposit sisa denda menjadi " + totalDenda + ".";
						} else {
							totalDenda = booking.get().getDeposit() - denda;
							note = "Pengembalian locker dengan denda sebesar " + denda + ". Sisa deposit setelah dipotong sebesar " + totalDenda;
						}
					}
					
					int isUpdate = bookingRepository.updateBooking(id, denda, note);
					
					if (isUpdate > 0) {
						return 1;
					} else {
						return 3;
					}
				} else {
					String note = "Pengembalian locker";
					
					List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.get().getBookingId());
					
					for (BookingDetail bookingDetail : bookingDetails) {
						webClientBuilder.build().post()
						.uri("http://localhost:8080/api/locker/update-inUse/" + bookingDetail.getLockerId() + "/0")
						.retrieve()
						.bodyToMono(Response.class)
						.block();
					}
					
					int isUpdate = bookingRepository.updateBooking(id, 0, note);
					
					if (isUpdate > 0) {
						return 1;
					} else {
						return 3;
					}
				}				
			}
			
			return 2;
		}
		
		return 0;
	}
	
	public List<BookingResponse> getAllBooking() {
		List<BookingResponse> resp = new ArrayList<BookingResponse>();
		
		List<Booking> booking = bookingRepository.findAll();
		
		for (Booking dataBooking : booking) {
			List<BookingDetail> bookingDetail = bookingDetailRepository.findByBookingId(dataBooking.getBookingId());
			List<BookingDetailResponse> bookingDetailResp = bookingDetail.stream().map(this::mapToBookingDetailResponse).toList();
			
			BookingResponse bookingResp = BookingResponse.builder()
					.bookingId(dataBooking.getBookingId())
					.userId(dataBooking.getUserId())
					.deposit(dataBooking.getDeposit())
					.denda(dataBooking.getDenda())
					.inUse(dataBooking.getInUse())
					.status(dataBooking.getStatus())
					.password(dataBooking.getPassword())
					.invalidPasswordAttempts(dataBooking.getInvalidPasswordAttempts())
					.costUnlockLocker(dataBooking.getCostUnlockLocker())
					.note(dataBooking.getNote())
					.startDate(dataBooking.getStartDate())
					.endDate(dataBooking.getEndDate())
					.bookingDetail(bookingDetailResp)
					.build();
			
			resp.add(bookingResp);
		}
		
		return resp;
	}
	
	private BookingDetailResponse mapToBookingDetailResponse(BookingDetail bookingDetail) {
		return BookingDetailResponse.builder()
				.bookingDetailId(bookingDetail.getBookingDetailId())
				.bookingId(bookingDetail.getBookingId())
				.lockerId(bookingDetail.getLockerId())
				.build();
	}
	
	private void sendEmail(String noLockerStr, String password, String email) {
		String subject = "Booking Locker";
		String to = email;
		String senderName = "Management";
		String from = emailFrom;
		
		String locker = "";
		
		String[] lockerArr = noLockerStr.trim().split(" ");
		
		for (int i = 0; i < lockerArr.length; i++) {
			locker = locker + "No. Loker " + lockerArr[i] + "<br>";
		}
		
		String content = "Dear Customer,<br>"
	            + "This is your locker number and password<br>"
				+ locker
				+ "password : " + password + "<br>"
	            + "Thank you,<br>"
	            + senderName;
		    
		MimeMessage message = mailSender.createMimeMessage();

		try {
		    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	    
	    	helper.setFrom(from, senderName);
	    	helper.setTo(to);
	    	helper.setSubject(subject);
	    	helper.setText(content, true);
		} catch (Exception e) {
			log.info("send email verification error. " , e);
		}	    

		mailSender.send(message);
		
		log.info("Send mail successfully");
	}
}
