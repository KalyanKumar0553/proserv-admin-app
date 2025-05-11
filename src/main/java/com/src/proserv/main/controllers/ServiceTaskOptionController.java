package com.src.proserv.main.controllers;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.proserv.main.configuration.JWTTokenProvider;
import com.src.proserv.main.request.dto.FAQRequestDTO;
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.TaskOptionService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.ServiceOptionValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/options")
public class ServiceTaskOptionController {


	final TaskOptionService optionService;

	final ServiceOptionValidator optionValidator;

	final JWTTokenProvider jwtTokenProvider;

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> createTOption(@RequestHeader("Authorization") String token,@Valid @RequestBody ServiceTaskOptionRequestDTO optionRequest)
			throws MessagingException {
		optionService.saveOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("FAQ Created Succesfully"));
	}
	
	
	@PutMapping("/{optionID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> updateOption(@RequestHeader("Authorization") String token,@PathVariable Long optionID,@Valid @RequestBody ServiceTaskOptionRequestDTO optionRequest)
			throws MessagingException {
		optionService.updateOption(jwtTokenProvider.getUserIDFromToken(token.substring(7)),optionID,optionRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("FAQ Update Succesfully"));
	}
	
}
