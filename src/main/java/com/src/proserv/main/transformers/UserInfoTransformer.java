package com.src.proserv.main.transformers;

import org.springframework.stereotype.Component;

import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.request.dto.SignupRequestDTO;
import com.src.proserv.main.services.UserDetailsServiceImpl;
import com.src.proserv.main.validators.AuthValidator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class UserInfoTransformer {

	final UserDetailsServiceImpl userService;

	final AuthValidator authValidator;

	public UserInfo fromSignupRequestDTO(SignupRequestDTO signupRequest) {
		UserInfo userInfo = UserInfo.builder().username(signupRequest.getUsername()).password(signupRequest.getPassword()).build();
		if(authValidator.isMobileNumberProivded(signupRequest)) {
			userInfo.setMobile(signupRequest.getMobile());
		} else {
			userInfo.setEmail(signupRequest.getEmail());
		}
		return userInfo;
	}
}
