package com.src.proserv.main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.LocationServiceTask;
import com.src.proserv.main.repository.LocationServiceTaskRepository;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.LocationTaskService;
import com.src.proserv.main.utils.AppUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value = "/api/location")
public class LocationServiceTaskController {

	final JWTTokenProvider tokenProvider;

	final LocationServiceTaskRepository locationTaskRepo;

	final LocationTaskService locationTaskService;
	
	@PostMapping("/{locationID}/task")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createLocationTask(@RequestBody LocationServiceTask locationTask,@RequestHeader("Authorization") String token) {
		if (locationTask.getLocationID() == null || locationTask.getLocationID()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Location ID");
		}
		if (locationTask.getLocationID() == null || locationTask.getLocationID()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Location ID");
		}
		return ResponseEntity.ok(AppUtils.getJSONObject(locationTaskService.save(locationTask, tokenProvider.getUserIDFromToken(token.substring(7)))));
	}

	@GetMapping("/{locationID}/tasks")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getAllServiceLocationTasks(@PathVariable Long locationID) {
		return ResponseEntity.ok(AppUtils.getJSONObject(locationTaskRepo.findAllByLocationID(locationID)));
	}

	@GetMapping("/{locationID}/task/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getServiceLocationTaskById(@PathVariable Long locationID,@PathVariable Long id) {
		return ResponseEntity.ok(AppUtils.getJSONObject(locationTaskRepo.findByLocationIDAndServiceTaskID(locationID,id)));
	}

	@PutMapping("/{locationID}/task")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateServiceLocationTask(@PathVariable Long locationID,@PathVariable Long taskid, @RequestBody LocationServiceTask locationTask,
			@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(AppUtils.getJSONObject(locationTaskService.update(locationID,taskid, locationTask, tokenProvider.getUserIDFromToken(token.substring(7)))));
	}

	@DeleteMapping("/{locationID}/tasks/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteServiceLocationTask(@PathVariable Long locationID,@PathVariable Long taskid) {
		locationTaskRepo.deleteByLocationIDAndServiceTaskID(locationID,taskid);
		return ResponseEntity.ok(AppUtils.getJSONObject("Task Deleted Succesfully"));
	}
}
