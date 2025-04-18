package com.src.proserv.main.controllers;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.proserv.main.configuration.JWTTokenProvider;
import com.src.proserv.main.request.dto.ServiceOptionRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.OptionService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.ServiceOptionValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/categories")
public class ServiceOptionController {


	final OptionService optionService;

	final ServiceOptionValidator optionValidator;

	final JWTTokenProvider jwtTokenProvider;

	@GetMapping("/{serviceCategoryID}/operations/{operationID}/options")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getOptions(@PathVariable Long serviceCategoryID,@PathVariable Long serviceOperationID,Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(optionService.fetchAllServiceOptions(serviceCategoryID,serviceOperationID)));
	}


	@PostMapping("/{serviceCategoryID}/operations/{operationID}/option")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createOption(@PathVariable Long serviceCategoryID,@PathVariable Long serviceOperationID,@RequestHeader("Authorization") String token,Authentication authentication,@RequestBody ServiceOptionRequestDTO optionRequestDTO)
			throws MessagingException {
		optionValidator.validateCreateOptionRequest(optionRequestDTO);
		return ResponseEntity.ok(AppUtils.getJSONObject(optionService.createServiceOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionRequestDTO)));
	}

	@PutMapping("/{serviceCategoryID}/operations/{operationID}/option")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateOption(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication,@RequestBody ServiceOptionRequestDTO optionRequestDTO)
			throws MessagingException {
		optionValidator.validateUpdateOptionRequest(optionRequestDTO);
    	return ResponseEntity.ok(AppUtils.getJSONObject(optionService.updateServiceOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionRequestDTO)));
	}

	@DeleteMapping("/{serviceCategoryID}/operations/{operationID}/option/{optionID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteOption(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long operationID,@PathVariable Long optionID,Authentication authentication)
			throws MessagingException {
		optionService.deleteServiceOption(optionID,serviceCategoryID,operationID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Category Succesfully Deleted"));
	}

}
