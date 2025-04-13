package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.exceptions.OTPException;
import com.src.proserv.main.exceptions.SignupException;
import com.src.proserv.main.exceptions.UserNotFoundException;
import com.src.proserv.main.exceptions.UserRequestException;
import com.src.proserv.main.model.ProServRoles;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.repository.ProServRolesRepository;
import com.src.proserv.main.repository.UserInfoRepository;
import com.src.proserv.main.request.dto.LoginRequestDTO;
import com.src.proserv.main.request.dto.SignupRequestDTO;
import com.src.proserv.main.request.dto.UserProfileRequestDTO;
import com.src.proserv.main.utils.AppConstants;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.utils.PasswordUtil;
import com.src.proserv.main.utils.RequestStatus;
import com.src.proserv.main.validators.AppValidators;
import com.src.proserv.main.validators.AuthValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final ProServRolesRepository rolesRepository;

	private final UserInfoRepository userRepository;

	private final MsgService emailService;

	private final OTPService otpService;

	private final AuthValidator authValidator;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		return new User(user.getUsername(), user.getPassword(), getAuthorities(user));
	}

	@Transactional
	public UserInfo saveUser(UserInfo user, PasswordEncoder passwordEncoder) {
		try {
			String salt = PasswordUtil.generateSalt();
			user.setSalt(salt);
			String hashPassword = PasswordUtil.hashPassword(user.getPassword(), salt);
			String encodedPassword = passwordEncoder.encode(hashPassword);
			user.setPassword(encodedPassword);
			String otp = generateOtp();
			user.setEnabled(false);
			String userID = generateUUID();
			user.setUUID(userID);
			user.setCreatedBy(userID);
			user.setCreatedOn(LocalDateTime.now());
			user.setLastModifiedBy(userID);
			user.setLastModifiedOn(LocalDateTime.now());
			Context context = new Context();
			context.setVariable("otp", otp);
			sendOTP(user,context);
			return userRepository.save(user);
		} catch(OTPException e ) {
			throw e;
		} catch(Exception e ) {
			e.printStackTrace();
			throw new SignupException(RequestStatus.SIGNUP_ERROR);
		}
	}

	public boolean sendOTP(UserInfo user,Context context) {
		if (otpService.canSendOtp(user.getUsername())) {
			if(AppValidators.isMobile(user.getUsername())) {
//				emailService.sendSMS("+19296006492", "+919148042308","Please User OTP : " + context.getVariable("otp") + " To login");
			} else {
//				emailService.sendHtmlEmail("DoNotReply@8fcd10ba-82fa-40fa-88ff-892b2c57d4bd.azurecomm.net",user.getEmail(), "MyComplex : Verification Mail", "otpEmail", context);
			}
			otpService.saveOtp(user.getUsername(), (String)context.getVariable("otp"));
			otpService.recordOtpAttempt(user.getUsername());
			return true;
		} else {
			throw new OTPException(RequestStatus.OTP_SENT_FAIL);
		}
	}

	public String generateOtp() {
		Random random = new Random();
		return "123456";
//		return String.format("%06d", random.nextInt(999999));
	}

	public Optional<UserInfo> findUserByUsername(String username) {
		return userRepository.findByEmailOrMobile(username, username);
	}

	public String verifyOtp(String username,String otp) {
		boolean isVerified = otpService.verifyOtp(username, otp);
		return (isVerified ? RequestStatus.OTP_VERIFICATION_SUCCESS : RequestStatus.OTP_VERIFICATION_FAIL).toString();
	}

	public String getUserName(SignupRequestDTO signupRequest) {
		return authValidator.isMobileNumberProivded(signupRequest) ? signupRequest.getMobile() : signupRequest.getEmail();
	}

	public void checkDuplicateUser(SignupRequestDTO signupRequest) {
		boolean isMobile = authValidator.isMobileNumberProivded(signupRequest);
		if(isMobile) {
			if(signupRequest.getMobile()!=null && !signupRequest.getMobile().trim().isEmpty()) {
				Optional<UserInfo> userWithMobile = userRepository.findByMobile(signupRequest.getMobile());
				if (userWithMobile.isPresent()) {
					throw new UserRequestException(RequestStatus.USER_DUPLICATE_MOBILE);
				}
			} else {
				throw new UserRequestException(RequestStatus.SIGNUP_REQUEST_MOBILE_DATA_ERROR);
			}
		} else {
			if(signupRequest.getEmail()!=null &&  !signupRequest.getEmail().trim().isEmpty()) {
				Optional<UserInfo> userWithEmail = userRepository.findByEmail(signupRequest.getEmail());
				if (userWithEmail.isPresent()) {
					throw new UserRequestException(RequestStatus.USER_DUPLICATE_EMAIL);
				}
			} else {
				throw new UserRequestException(RequestStatus.SIGNUP_REQUEST_EMAIL_DATA_ERROR);
			}
		}
	}

	public Set<GrantedAuthority> getAuthorities(UserInfo user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		Optional<ProServRoles> proservAdmin = rolesRepository.findByUserUUID(user.getUUID());
		if(proservAdmin.isPresent()) {
			String[] roles = proservAdmin.get().getRoles().split(AppConstants.roleSeperator);
			for(String currRole : roles) {
				authorities.add(new SimpleGrantedAuthority(currRole));
			}
		}
		return authorities;
	}

	public UserInfo validateAndGetIfUserEnabled(LoginRequestDTO loginRequest) {
		UserInfo user = findUserByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new UserNotFoundException(RequestStatus.USER_NOT_FOUND));
		if (!user.isEnabled()) {
			String usernameSrc = user.getUsername().equalsIgnoreCase(user.getEmail()) ? "email" : "mobile";
			log.info("usernameSrc : {}", usernameSrc);
			throw new UserNotFoundException(RequestStatus.OTP_NOT_VERIFIED_ERROR, usernameSrc);
		}
		return user;
	}

	private String generateUUID() {
		while(true) {
			List<String> uuidList = AppUtils.generateRandomUUID();
			List<String> existingUserIds = userRepository.findExistingUserIds(uuidList);
			Set<String> existingSet = new HashSet<>(existingUserIds);
	        for (String id : uuidList) {
	            if (!existingSet.contains(id)) {
	                return id;
	            }
	        }
		}
	}

	public void resetPasswordWithOTP(UserInfo user,String otp,String password,PasswordEncoder passwordEncoder) {
		if(verifyOtp(user.getUsername(), otp).equals(RequestStatus.OTP_VERIFICATION_SUCCESS.toString())) {
			String salt = PasswordUtil.generateSalt();
			user.setSalt(salt);
			String hashPassword = PasswordUtil.hashPassword(password, salt);
			String encodedPassword = passwordEncoder.encode(hashPassword);
			user.setPassword(encodedPassword);
			user.setLastModifiedBy(user.getUUID());
			user.setLastModifiedOn(LocalDateTime.now());
			userRepository.save(user);
		} else {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(),"Error: Failed To Reset Password");
		}

	}

	public void resetPasswordWithoutOTP(UserInfo user,String password,PasswordEncoder passwordEncoder) {
		String salt = PasswordUtil.generateSalt();
		user.setSalt(salt);
		String hashPassword = PasswordUtil.hashPassword(password, salt);
		String encodedPassword = passwordEncoder.encode(hashPassword);
		user.setPassword(encodedPassword);
		user.setLastModifiedBy(user.getUUID());
		user.setLastModifiedOn(LocalDateTime.now());
		userRepository.save(user);
	}

	public void updateUserProfile(UserInfo user, UserProfileRequestDTO profileRequest) {
		user.setFullName(profileRequest.getFullName());
		if(profileRequest.getEmail()!=null) {
			user.setEmail(profileRequest.getEmail());
		} if(profileRequest.getMobile()!=null) {
			user.setMobile(profileRequest.getMobile());
		} if(profileRequest.getDob()!=null) {
			user.setDob(profileRequest.getDob());
		} if(profileRequest.getFullName()!=null) {
			user.setFullName(profileRequest.getFullName());
		} if(profileRequest.getProfilePic()!=null) {
			user.setProfilePic(profileRequest.getProfilePic());
		}
		user.setLastModifiedOn(LocalDateTime.now());
		userRepository.save(user);
	}
}
