package com.src.proserv.main.controllers;

import javax.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.OverviewService;
import com.src.proserv.main.utils.AppUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class OverviewController {
	
	final OverviewService overviewService;
	
	
	
	@GetMapping("/overview")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getOverviewData(Authentication authentication) throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(overviewService.getOverviewData()));
	}
	
}
