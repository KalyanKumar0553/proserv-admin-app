package com.src.proserv.main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.proserv.main.configuration.JWTTokenProvider;
import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.exceptions.UserNotFoundException;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.request.dto.ResetPasswordWithoutOTPRequestDTO;
import com.src.proserv.main.request.dto.UserProfileRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.UserDetailsServiceImpl;
import com.src.proserv.main.transformers.UserInfoTransformer;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.utils.RequestStatus;
import com.src.proserv.main.validators.AuthValidator;
import com.src.proserv.main.validators.UserValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value ="/api")
public class UserProfileController {

	final AuthenticationManager authenticationManager;

	final JWTTokenProvider tokenProvider;

	final UserDetailsServiceImpl userService;

	final PasswordEncoder passwordEncoder;

	final AuthValidator authValidator;

	final UserValidator userValidator;

	final UserInfoTransformer userInfoTransformer;

	final JWTTokenProvider jwtTokenProvider;

	@PostMapping("/update")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateProfile(@RequestBody UserProfileRequestDTO userProfileRequest,Authentication authentication) {
		String username = authentication.getName();
		UserInfo user = userService.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		userValidator.validateUserProfileRequest(userProfileRequest,user);
		userService.updateUserProfile(user,userProfileRequest);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Loggedout").isError(false).build());
    }
	

	@PostMapping("/reset_password_without_otp")
	@PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> sendOtp(@RequestBody ResetPasswordWithoutOTPRequestDTO resetPasswordRequest,Authentication authentication) {
		String username = authentication.getName();
		authValidator.validateResetPasswordWithoutOTPRequest(resetPasswordRequest,username);
		UserInfo user = userService.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		if(!user.getUsername().equals(username)) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"user don't have access to update paassword");
		}
		userService.resetPasswordWithoutOTP(user,resetPasswordRequest.getPassword(),passwordEncoder);
		return ResponseEntity.ok(AppUtils.getJSONObject(RequestStatus.PASSWORD_RESET_SUCCESS.getDescription()));
	}
}
