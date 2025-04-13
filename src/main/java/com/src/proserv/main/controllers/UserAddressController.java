package com.src.proserv.main.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.proserv.main.request.dto.UserAddressRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.UserAddressService;
import com.src.proserv.main.validators.UserAddressValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value = {"/api/user/address"})
public class UserAddressController {

	final AuthenticationManager authenticationManager;

	final UserAddressValidator addressValidator;

	final UserAddressService addressService;


	@PostMapping("")
	public ResponseEntity<JSONResponseDTO<?>> addUserAddress(@RequestBody UserAddressRequestDTO userAddressRequest,Authentication authentication) {
		addressValidator.validateUserAddress(userAddressRequest, authentication.getName());
		addressService.addUserAddress(userAddressRequest);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Succesfully Added Address").isError(false).build());
    }

	@GetMapping("")
	public ResponseEntity<JSONResponseDTO<?>> getUserAddresses(Authentication authentication) {
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg(addressService.getUserAddress(authentication.getName())).isError(false).build());
    }

	@DeleteMapping("/{addressID}")
	public ResponseEntity<JSONResponseDTO<?>> deleteAddress(@PathVariable Long addressID,Authentication authentication) {
		addressService.deleteUserAddress(addressID);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Succesfully Added Address").isError(false).build());
    }

	@PutMapping("/{addressID}")
	public ResponseEntity<JSONResponseDTO<?>> updateAddress(@PathVariable Long addressID,@RequestBody UserAddressRequestDTO userAddressRequest,Authentication authentication) {
		addressService.updateUserAddress(addressID,userAddressRequest);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Succesfully Added Address").isError(false).build());
    }
}
