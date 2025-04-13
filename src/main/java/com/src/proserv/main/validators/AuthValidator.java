package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.InvalidRequestException;
import com.src.proserv.main.request.dto.LoginRequestDTO;
import com.src.proserv.main.request.dto.OTPVerificationRequestDTO;
import com.src.proserv.main.request.dto.ResetPasswordRequestDTO;
import com.src.proserv.main.request.dto.ResetPasswordWithoutOTPRequestDTO;
import com.src.proserv.main.request.dto.SendOTPRequestDTO;
import com.src.proserv.main.request.dto.SignupRequestDTO;
import com.src.proserv.main.utils.RequestStatus;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthValidator {

	public void validateSignUpRequest(SignupRequestDTO signupRequest) {
		signupRequest.setEmail("");
		signupRequest.setMobile("");
		Optional<String> username = Optional.ofNullable(signupRequest.getUsername());
		Optional<String> password = Optional.ofNullable(signupRequest.getPassword());
		if (username.isEmpty() || username.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_USERNAME_REQUIRED_ERROR);
		}
		if (password.isEmpty() || password.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_PASSWORD_REQUIRED_ERROR);
		}
		if (AppValidators.isEmail(username.get())) {
			signupRequest.setEmail(username.get());
		} else if (AppValidators.isMobile(username.get())) {
			signupRequest.setMobile(username.get());
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}


	public void validateLoginRequest(LoginRequestDTO loginRequest) {
		loginRequest.setEmail("");
		loginRequest.setMobile("");
		Optional<String> username = Optional.ofNullable(loginRequest.getUsername());
		Optional<String> password = Optional.ofNullable(loginRequest.getPassword());
		if (username.isEmpty() || username.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_USERNAME_REQUIRED_ERROR);
		}
		if (password.isEmpty() || password.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_PASSWORD_REQUIRED_ERROR);
		}
		if (AppValidators.isEmail(username.get())) {
			loginRequest.setEmail(username.get());
		} else if (AppValidators.isMobile(username.get())) {
			loginRequest.setMobile(username.get());
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}

	public void validateOTPVerificationRequest(OTPVerificationRequestDTO otpVerificationRequest) {
		otpVerificationRequest.setEmail("");
		otpVerificationRequest.setMobile("");
		Optional<String> username = Optional.ofNullable(otpVerificationRequest.getUsername());
		Optional<String> otp = Optional.ofNullable(otpVerificationRequest.getOtp());
		if (username.isEmpty() || username.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.OTP_VERIFICATION_ERROR_USERNAME_REQUIRED_ERROR);
		}
		if (otp.isEmpty() || otp.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.OTP_VERIFICATION_ERROR_OTP_REQUIRED);
		}
		if(otp.get().trim().length()!=6) {
			throw new InvalidRequestException(RequestStatus.VERIFICATION_REQUEST_OTP_ERROR);
		}
		if (AppValidators.isEmail(username.get())) {
			otpVerificationRequest.setEmail(username.get());
		} else if (AppValidators.isMobile(username.get())) {
			otpVerificationRequest.setMobile(username.get());
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}

	public void validateSendOTPRequest(SendOTPRequestDTO sendOTPRequest) {
		sendOTPRequest.setEmail("");
		sendOTPRequest.setMobile("");
		Optional<String> username = Optional.ofNullable(sendOTPRequest.getUsername());
		if (username.isEmpty() || username.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.SENDOTP_ERROR_USERNAME_REQUIRED_ERROR);
		}
		if (AppValidators.isEmail(username.get())) {
			sendOTPRequest.setEmail(username.get());
		} else if (AppValidators.isMobile(username.get())) {
			sendOTPRequest.setMobile(username.get());
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}

	public void validateResetPasswordWithOTPRequest(ResetPasswordRequestDTO resetPasswordRequest) {
		resetPasswordRequest.setEmail("");
		resetPasswordRequest.setMobile("");
		Optional<String> username = Optional.ofNullable(resetPasswordRequest.getUsername());
		Optional<String> password = Optional.ofNullable(resetPasswordRequest.getPassword());
		Optional<String> retypePassword = Optional.ofNullable(resetPasswordRequest.getRetypePassword());
		Optional<String> otp = Optional.ofNullable(resetPasswordRequest.getOtp());
		if (username.isEmpty() || username.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_USERNAME_REQUIRED);
		}
		if (password.isEmpty() || password.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_PASSWORD_REQUIRED);
		}
		if (retypePassword.isEmpty() || retypePassword.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_RETYPE_PASSWORD_REQUIRED);
		}
		if (otp.isEmpty() || otp.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_OTP_REQUIRED);
		}
		if(!password.equals(retypePassword)) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_PASSWORD_NOT_MATCH);
		}
		if (AppValidators.isEmail(username.get())) {
			resetPasswordRequest.setEmail(username.get());
		} else if (AppValidators.isMobile(username.get())) {
			resetPasswordRequest.setMobile(username.get());
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}


	public void validateResetPasswordWithoutOTPRequest(ResetPasswordWithoutOTPRequestDTO resetPasswordRequest,String username) {
		resetPasswordRequest.setEmail("");
		resetPasswordRequest.setMobile("");
		Optional<String> password = Optional.ofNullable(resetPasswordRequest.getPassword());
		Optional<String> retypePassword = Optional.ofNullable(resetPasswordRequest.getRetypePassword());
		if (password.isEmpty() || password.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_PASSWORD_REQUIRED);
		}
		if (retypePassword.isEmpty() || retypePassword.get().isEmpty()) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_RETYPE_PASSWORD_REQUIRED);
		}
		if(!password.equals(retypePassword)) {
			throw new InvalidRequestException(RequestStatus.RESET_PASSWORD_ERROR_PASSWORD_NOT_MATCH);
		}
		if (AppValidators.isEmail(username)) {
			resetPasswordRequest.setEmail(username);
		} else if (AppValidators.isMobile(username)) {
			resetPasswordRequest.setMobile(username);
		} else {
			throw new InvalidRequestException(RequestStatus.LOGIN_REQUEST_INVALID_USERNAME_ERROR);
		}
	}


	public boolean isMobileNumberProivded(SignupRequestDTO signupRequest) {
		return AppValidators.isMobile(signupRequest.getUsername());
	}

	public boolean isMobileNumberProivded(OTPVerificationRequestDTO otpRequest) {
		return AppValidators.isMobile(otpRequest.getUsername());
	}
}
