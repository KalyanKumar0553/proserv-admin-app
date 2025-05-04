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
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.CategoryService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.OperationValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service")
public class ServiceCategoryController {

	
	final OperationValidator operationValidator;
	
	final JWTTokenProvider jwtTokenProvider;
	
	final CategoryService categoryService;
	
	@GetMapping("/categories")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getCategories(Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(categoryService.fetchAllServiceCategories()));
	}
	
	@GetMapping("/categories/{categoryID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getCategoriesByID(Authentication authentication,@PathVariable Long categoryID)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(categoryService.fetchServiceCategoryByID(categoryID)));
	}
	
	
	@PostMapping("/categories")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createCategory(@RequestHeader("Authorization") String token,Authentication authentication,@RequestBody ServiceCategoryRequestDTO categoryRequestDTO)
			throws MessagingException {
		operationValidator.validateCreateCategoryRequest(categoryRequestDTO);
		return ResponseEntity.ok(AppUtils.getJSONObject(categoryService.createServiceCategory(jwtTokenProvider.getUserIDFromToken(token.substring(7)),categoryRequestDTO)));
	}
	
	@PutMapping("/categories/{categoryID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateCategory(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication,@RequestBody ServiceCategoryRequestDTO categoryRequestDTO)
			throws MessagingException {
		operationValidator.validateUpdateCategoryRequest(categoryRequestDTO);
    	return ResponseEntity.ok(AppUtils.getJSONObject(categoryService.updateServiceCategory(jwtTokenProvider.getUserIDFromToken(token.substring(7)),categoryRequestDTO)));
	}

	@DeleteMapping("/categories/{categoryID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteCategory(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication)
			throws MessagingException {
		categoryService.deleteServiceCategory(serviceCategoryID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Category Succesfully Deleted"));
	}
	
}
