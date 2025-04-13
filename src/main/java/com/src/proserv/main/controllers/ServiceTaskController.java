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
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.TaskService;
import com.src.proserv.main.utils.AppUtils;
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

	@GetMapping("/{serviceCategoryID}/operations/{operationID}/options/{optionID}/tasks")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getTasks(@PathVariable Long serviceCategoryID,@PathVariable Long serviceOperationID,@PathVariable Long optionID,Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(taskService.fetchAllServiceTasks(serviceCategoryID,serviceOperationID,optionID)));
	}


	@PostMapping("/{serviceCategoryID}/operations/{operationID}/option/{optionID}/task")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createTask(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long serviceOperationID,@PathVariable Long optionID,Authentication authentication,@RequestBody ServiceTaskRequestDTO taskRequestDTO)
			throws MessagingException {
		taskValidator.validateCreateTaskRequest(taskRequestDTO);
		return ResponseEntity.ok(AppUtils.getJSONObject(taskService.createServiceTask(jwtTokenProvider.getUserIDFromToken(token.substring(7)),taskRequestDTO)));
	}

	@PutMapping("/{serviceCategoryID}/operations/{operationID}/option/{optionID}/task")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateTask(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long serviceOperationID,@PathVariable Long optionID,Authentication authentication,@RequestBody ServiceTaskRequestDTO taskRequestDTO)
			throws MessagingException {
		taskValidator.validateUpdateTaskRequest(taskRequestDTO);
    	return ResponseEntity.ok(AppUtils.getJSONObject(taskService.updateServiceTask(jwtTokenProvider.getUserIDFromToken(token.substring(7)),taskRequestDTO)));
	}

	@DeleteMapping("/{serviceCategoryID}/operations/{operationID}/option/{optionID}/task/{taskID}")
	@PreAuthorize("hasAnyRole('SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteCategory(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long operationID,@PathVariable Long optionID,@PathVariable Long taskID,Authentication authentication)
			throws MessagingException {
		taskService.deleteServiceTask(taskID,serviceCategoryID,operationID,optionID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Task Succesfully Deleted"));
	}

}
