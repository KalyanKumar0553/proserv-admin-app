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
import com.src.proserv.main.request.dto.ServiceCategoryRequestDTO;
import com.src.proserv.main.request.dto.ServiceOperationRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.OperationService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.ServiceOperationValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/categories")
public class ServiceOperationController {


	final OperationService operationService;

	final ServiceOperationValidator operationValidator;

	final JWTTokenProvider jwtTokenProvider;

	@GetMapping("/{serviceCategoryID}/operations")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getOperations(@PathVariable Long serviceCategoryID,Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(operationService.fetchAllServiceOperations(serviceCategoryID)));
	}


	@PostMapping("/{serviceCategoryID}/operation")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createOperation(@PathVariable Long serviceCategoryID,@RequestHeader("Authorization") String token,Authentication authentication,@RequestBody ServiceOperationRequestDTO operationRequestDTO)
			throws MessagingException {
		operationValidator.validateCreateOperationRequest(operationRequestDTO);
		return ResponseEntity.ok(AppUtils.getJSONObject(operationService.createServiceOperation(jwtTokenProvider.getUserIDFromToken(token.substring(7)),operationRequestDTO)));
	}

	@PutMapping("/{serviceCategoryID}/operation")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateOperation(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication,@RequestBody ServiceOperationRequestDTO operationRequestDTO)
			throws MessagingException {
		operationValidator.validateUpdateOperationRequest(operationRequestDTO);
    	return ResponseEntity.ok(AppUtils.getJSONObject(operationService.updateServiceOperation(jwtTokenProvider.getUserIDFromToken(token.substring(7)),operationRequestDTO)));
	}

	@DeleteMapping("/{serviceCategoryID}/categories/{operationID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteOperation(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long operationID,Authentication authentication)
			throws MessagingException {
		operationService.deleteServiceOperation(operationID,serviceCategoryID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Category Succesfully Deleted"));
	}

}
