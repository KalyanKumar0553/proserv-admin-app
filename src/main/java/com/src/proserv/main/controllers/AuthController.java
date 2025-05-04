package com.src.proserv.main.controllers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.src.proserv.main.configuration.JWTTokenProvider;
import com.src.proserv.main.exceptions.UserNotFoundException;
import com.src.proserv.main.model.InvalidatedToken;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.request.dto.LoginRequestDTO;
import com.src.proserv.main.request.dto.OTPVerificationRequestDTO;
import com.src.proserv.main.request.dto.ResetPasswordRequestDTO;
import com.src.proserv.main.request.dto.SendOTPRequestDTO;
import com.src.proserv.main.request.dto.SignupRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.response.dto.JwtAuthenticationResponseDTO;
import com.src.proserv.main.services.UserDetailsServiceImpl;
import com.src.proserv.main.transformers.UserInfoTransformer;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.utils.PasswordUtil;
import com.src.proserv.main.utils.RequestStatus;
import com.src.proserv.main.validators.AuthValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

	final AuthenticationManager authenticationManager;

	final JWTTokenProvider tokenProvider;

	final UserDetailsServiceImpl userService;

	final PasswordEncoder passwordEncoder;

	final AuthValidator authValidator;

	final UserInfoTransformer userInfoTransformer;

	final JWTTokenProvider jwtTokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<JSONResponseDTO<?>> registerUser(@RequestBody SignupRequestDTO signupRequest)
			throws MessagingException {
		authValidator.validateSignUpRequest(signupRequest);
		userService.checkDuplicateUser(signupRequest);
		String msgSource = authValidator.isMobileNumberProivded(signupRequest) ? "mobile" : "email";
		UserInfo userInfo = userInfoTransformer.fromSignupRequestDTO(signupRequest);
		userService.saveUser(userInfo, passwordEncoder);
		String msg = String.format(RequestStatus.SIGNUP_SUCCESS.getDescription(msgSource));
		return ResponseEntity.ok(AppUtils.getJSONObject(msg));
	}

	@PostMapping("/login")
	public ResponseEntity<JSONResponseDTO<?>> authenticateUser(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
		authValidator.validateLoginRequest(loginRequest);
		UserInfo user = userService.validateAndGetIfUserEnabled(loginRequest);
		String hashPassword = PasswordUtil.hashPassword(loginRequest.getPassword(), user.getSalt());
		String encodedPassword = passwordEncoder.encode(hashPassword);
		if (!passwordEncoder.matches(hashPassword, encodedPassword)) {
			throw new UserNotFoundException(RequestStatus.BAD_CREDENTIALS);
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				user.getUsername(), hashPassword, userService.getAuthorities(user));
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		String token = tokenProvider.generateToken(authentication, user.getUUID());
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		return ResponseEntity.ok(AppUtils.getJSONObject(new JwtAuthenticationResponseDTO(token)));
	}

	@PostMapping("/verify_otp")
	public ResponseEntity<JSONResponseDTO<?>> verifyOtp(@RequestBody OTPVerificationRequestDTO otpVerificationRequest) {
		authValidator.validateOTPVerificationRequest(otpVerificationRequest);
		UserInfo user = userService.findUserByUsername(otpVerificationRequest.getUsername())
				.orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		if (user.isEnabled()) {
			return ResponseEntity
					.ok(AppUtils.getJSONObject(RequestStatus.USER_ALREADY_VERIFIED_ERROR.getDescription()));
		}
		String msg = userService.verifyOtp(otpVerificationRequest.getUsername(), otpVerificationRequest.getOtp());
		return ResponseEntity.ok(AppUtils.getJSONObject(msg));
	}

	@PostMapping("/send_otp")
	public ResponseEntity<JSONResponseDTO<?>> sendOtp(@RequestBody SendOTPRequestDTO sendOTPRequest) {
		authValidator.validateSendOTPRequest(sendOTPRequest);
		String username = sendOTPRequest.getUsername();
		UserInfo user = userService.findUserByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		Context context = new Context();
		context.setVariable("otp", userService.generateOtp());
		boolean otpSent = userService.sendOTP(user, context);
		return ResponseEntity.ok(AppUtils.getJSONObject(otpSent ? RequestStatus.OTP_SENT_SUCCESS.getDescription()
				: RequestStatus.OTP_SENT_FAIL.getDescription()));
	}

	@GetMapping("/roles")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER','AGENT')")
	public ResponseEntity<JSONResponseDTO<?>> getRoles(Authentication authentication) throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(authentication.getAuthorities()));
	}
	

	@PostMapping("/reset_password_with_otp")
	public ResponseEntity<JSONResponseDTO<?>> resetPasswordWithOTP(
			@RequestBody ResetPasswordRequestDTO resetPasswordRequest, Authentication authentication) {
		authValidator.validateResetPasswordWithOTPRequest(resetPasswordRequest);
		UserInfo user = userService.findUserByUsername(resetPasswordRequest.getUsername())
				.orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		userService.resetPasswordWithOTP(user, resetPasswordRequest.getOtp(), resetPasswordRequest.getPassword(),
				passwordEncoder);
		return ResponseEntity.ok(AppUtils.getJSONObject(RequestStatus.PASSWORD_RESET_SUCCESS.getDescription()));
	}

	@PostMapping("/logout")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> logout(@RequestHeader("Authorization") String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			token = token.substring(7);
			jwtTokenProvider.invalidateToken(token);
			return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Loggedout").isError(false).build());
		} else {
			return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("Unable To Logout").isError(true).build());
		}
	}
}