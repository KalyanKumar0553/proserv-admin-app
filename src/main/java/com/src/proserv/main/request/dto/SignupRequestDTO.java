package com.src.proserv.main.request.dto;

import lombok.Data;

@Data
public class SignupRequestDTO {
	private String username;
	private String email;
	private String mobile;
	private String password;
	private String retypePassword;
	private String otp;
}
