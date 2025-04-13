package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.UserAddress;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.repository.UserAddressRepository;
import com.src.proserv.main.repository.UserInfoRepository;
import com.src.proserv.main.request.dto.UserAddressRequestDTO;
import com.src.proserv.main.response.dto.UserAddressResponseDTO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@AllArgsConstructor
public class UserAddressService {

	private final UserInfoRepository userRepository;

	private final UserAddressRepository userAddressRepository;

	public List<UserAddressResponseDTO> getUserAddress(String username) {
		Optional<UserInfo> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "User Not Available");
		}
		UserInfo currUser = user.get();
		return userAddressRepository.findAllByUserUUID(currUser.getUUID()).stream().map(UserAddressResponseDTO::fromEntityToUserAddressResponsetDTO).collect(Collectors.toList());
	}

	public void updateUserAddress(Long addressID,UserAddressRequestDTO addressRequest) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<UserInfo> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "User Not Available");
		}
		Optional<UserAddress> userAddressHolder = userAddressRepository.findByAddressIDAndUserUUID(addressID,user.get().getUUID());
		if(userAddressHolder.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Address Not found with details");
		}
		UserAddress currAddress = userAddressHolder.get();
		UserAddressRequestDTO.toEntityFromUserAddressRequestDTO(currAddress,addressRequest,user.get());
		userAddressRepository.save(currAddress);
	}

	public void addUserAddress(UserAddressRequestDTO addressRequest) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<UserInfo> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "User Not Available");
		}
		UserAddress currAddress = new UserAddress();
		UserAddressRequestDTO.toEntityFromUserAddressRequestDTO(currAddress,addressRequest,user.get());
		userAddressRepository.save(currAddress);
	}

	public void deleteUserAddress(Long addressID) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<UserInfo> user = userRepository.findByUsername(username);
		if(user.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "User Not Available");
		}
		Optional<UserAddress> userAddressHolder = userAddressRepository.findByAddressIDAndUserUUID(addressID,user.get().getUUID());
		if(userAddressHolder.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Address Not found with details");
		}
		userAddressRepository.delete(userAddressHolder.get());
	}

}
