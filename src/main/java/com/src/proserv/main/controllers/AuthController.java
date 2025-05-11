package com.src.proserv.main.controllers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.src.proserv.main.request.dto.LoginRequestDTO;
import com.src.proserv.main.request.dto.OTPVerificationRequestDTO;
import com.src.proserv.main.request.dto.ResetPasswordRequestDTO;
import com.src.proserv.main.request.dto.SendOTPRequestDTO;
import com.src.proserv.main.request.dto.SignupRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.AuthService;
import com.src.proserv.main.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

	final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<JSONResponseDTO<?>> registerUser(@RequestBody SignupRequestDTO signupRequest)
			throws MessagingException {
		return authService.signup(signupRequest);
	}

	@PostMapping("/login")
	public ResponseEntity<JSONResponseDTO<?>> authenticateUser(@RequestBody LoginRequestDTO loginRequest,
			HttpServletResponse response) {
		return authService.login(loginRequest, response);
	}

	@PostMapping("/verify_otp")
	public ResponseEntity<JSONResponseDTO<?>> verifyOtp(@RequestBody OTPVerificationRequestDTO otpVerificationRequest) {
		return authService.verifyOTP(otpVerificationRequest);
	}

	@PostMapping("/send_otp")
	public ResponseEntity<JSONResponseDTO<?>> sendOtp(@RequestBody SendOTPRequestDTO sendOTPRequest) {
		return authService.sendOTP(sendOTPRequest);
	}

	@GetMapping("/roles")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER','AGENT')")
	public ResponseEntity<JSONResponseDTO<?>> getRoles(Authentication authentication) throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(authentication.getAuthorities()));
	}

	@PostMapping("/reset_password_with_otp")
	public ResponseEntity<JSONResponseDTO<?>> resetPasswordWithOTP(
			@RequestBody ResetPasswordRequestDTO resetPasswordRequest, Authentication authentication) {
		return authService.resetPasswordWithOTP(authentication,resetPasswordRequest);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<JSONResponseDTO<?>> refreshAccessToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
		return authService.refreshToken(refreshToken,response);
	}

	@PostMapping("/logout")
	public ResponseEntity<JSONResponseDTO<?>> logout(@RequestHeader("Authorization") String accessToken, @CookieValue(name = "refreshToken", required = false) String refreshToken,HttpServletResponse response) {
		return authService.logout(accessToken,refreshToken,response);
	}
}