package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.OTPException;
import com.src.proserv.main.model.Otp;
import com.src.proserv.main.model.OtpAttempt;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.repository.OtpAttemptRepository;
import com.src.proserv.main.repository.OtpRepository;
import com.src.proserv.main.repository.UserInfoRepository;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.utils.RequestStatus;

@Service
public class OTPService {
	private static final int MAX_ATTEMPTS = 5;
	private static final int OTP_VALIDITY_MINUTES = 3;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private OtpAttemptRepository otpAttemptRepository;

	@Autowired
	private UserInfoRepository userRepository;

	public boolean canSendOtp(String username) {
		LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfToday = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
		Optional<OtpAttempt> todayOTPAttempt = otpAttemptRepository.findByUsernameAndCreatedAtBetweenOrderByCreatedAtAsc(username,startOfToday, endOfToday);
		if (todayOTPAttempt.isPresent()) {
			if (todayOTPAttempt.get().getAttempts() >= MAX_ATTEMPTS) {
				throw new OTPException(RequestStatus.OTP_LIMIT_EXCEED_ERROR);
			}

			LocalDateTime otpSentAt = todayOTPAttempt.get().getCreatedAt();
			LocalDateTime nextOtpAt = otpSentAt.plusMinutes(OTP_VALIDITY_MINUTES);
			LocalDateTime currentTime =  LocalDateTime.now();

			System.out.println("Current Time : " + LocalDateTime.now());
			System.out.println("OTP Sent Time : " + otpSentAt);
			System.out.println("Limit Time : " + nextOtpAt);


			boolean isLessThanValidityMinutes = nextOtpAt.isBefore(currentTime);

			System.out.println("isLessThanValidityMinutes : " + isLessThanValidityMinutes);

			if (isLessThanValidityMinutes) {
				throw new OTPException(RequestStatus.OTP_TIME_LIMIT_ERROR,AppUtils.formatSecondsToHMString(ChronoUnit.SECONDS.between(nextOtpAt,otpSentAt)));
			}
		}
		return true;
	}

	public void recordOtpAttempt(String username) {
		LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfToday = LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX);
		Optional<OtpAttempt> otpAttemptOpt = otpAttemptRepository.findByUsernameAndCreatedAtBetweenOrderByCreatedAtAsc(username,startOfToday, endOfToday);
		if (otpAttemptOpt.isPresent()) {
			OtpAttempt otpAttempt = otpAttemptOpt.get();
			otpAttempt.setAttempts(otpAttempt.getAttempts() + 1);
			otpAttemptRepository.save(otpAttempt);
		} else {
			OtpAttempt otpAttempt = new OtpAttempt();
			otpAttempt.setUsername(username);
			otpAttempt.setCreatedAt(LocalDateTime.now());
			otpAttempt.setAttempts(1);
			otpAttemptRepository.save(otpAttempt);
		}
	}

	public String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}

	public void saveOtp(String username, String otp) {
		Otp otpEntity = new Otp();
		otpEntity.setUsername(username);
		otpEntity.setOtp(otp);
		otpEntity.setCreatedAt(LocalDateTime.now());
		otpRepository.save(otpEntity);
	}

	public boolean verifyOtp(String username, String otp) {
		Optional<Otp> otpOpt = otpRepository.findFirstByUsernameOrderByCreatedAtDesc(username);
		if (otpOpt.isPresent()) {
			Otp otpEntity = otpOpt.get();
			if (otpEntity.getOtp().equals(otp)) {
				Optional<UserInfo> user = userRepository.findByEmailOrMobile(username, username);
				user.ifPresent(u -> {
					u.setEnabled(true);
					userRepository.save(u);
					otpRepository.deleteAllByUsername(username);
					otpAttemptRepository.deleteAllByUsername(username);
				});
				return true;
			} else {
				throw new OTPException(RequestStatus.OTP_VERIFICATION_FAIL);
			}
		} else {
			throw new RuntimeException();
		}
	}
}
