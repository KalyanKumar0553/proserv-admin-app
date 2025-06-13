package com.src.proserv.main.controllers;

import javax.mail.MessagingException;
import javax.validation.Valid;

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
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.TaskOptionService;
import com.src.proserv.main.services.TaskService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.ServiceOptionValidator;
import com.src.proserv.main.validators.ServiceTaskValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/categories")
public class ServiceTaskController {


	final TaskService taskService;

	final ServiceTaskValidator taskValidator;

	final JWTTokenProvider jwtTokenProvider;
	
	final TaskOptionService optionService;

	final ServiceOptionValidator optionValidator;


	@GetMapping("/{serviceCategoryID}/tasks")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getTasks(@PathVariable Long serviceCategoryID,Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(taskService.fetchAllServiceTasks(serviceCategoryID)));
	}
	
	@GetMapping("/{serviceCategoryID}/tasks/{serviceTaskID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getTaskById(@PathVariable Long serviceCategoryID,@PathVariable Long serviceTaskID,Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(taskService.fetchServiceTaskByID(serviceCategoryID,serviceTaskID)));
	}


	@PostMapping("/{serviceCategoryID}/tasks")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> createTask(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication,@RequestBody ServiceTaskRequestDTO taskRequestDTO)
			throws MessagingException {
		taskValidator.validateCreateTaskRequest(taskRequestDTO);
		return ResponseEntity.ok(AppUtils.getJSONObject(taskService.createServiceTask(jwtTokenProvider.getUserIDFromToken(token.substring(7)),taskRequestDTO)));
	}
	
	@DeleteMapping("{serviceCategoryID}/tasks/{serviceTaskID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> deleteCategory(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long serviceTaskID,Authentication authentication)
			throws MessagingException {
		taskService.deleteServiceTask(serviceCategoryID,serviceTaskID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Category Succesfully Deleted"));
	}
	
	@PutMapping("{serviceCategoryID}/tasks/{serviceTaskID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateTask(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,Authentication authentication,@RequestBody ServiceTaskRequestDTO taskRequestDTO)
			throws MessagingException {
		taskValidator.validateUpdateTaskRequest(taskRequestDTO);
    	return ResponseEntity.ok(AppUtils.getJSONObject(taskService.updateServiceTask(jwtTokenProvider.getUserIDFromToken(token.substring(7)),taskRequestDTO)));
	}
	
	@PostMapping("/{serviceCategoryID}/options")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> createOption(@RequestHeader("Authorization") String token,@Valid @RequestBody ServiceTaskOptionRequestDTO optionRequest)
			throws MessagingException {
		optionValidator.validateCreateOptionRequest(optionRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject(optionService.saveOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionRequest)));
	}
	
	@PutMapping("/{serviceCategoryID}/options/{optionID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> updateOption(@RequestHeader("Authorization") String token,@PathVariable Long optionID,@Valid @RequestBody ServiceTaskOptionRequestDTO optionRequest)
			throws MessagingException {
		optionValidator.validateUpdateOptionRequest(optionRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject(optionService.updateOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionID,optionRequest)));
	}
}
