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
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.FAQService;
import com.src.proserv.main.utils.AppUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/faqs")
public class FAQController {


	final FAQService faqService;

	final JWTTokenProvider jwtTokenProvider;


	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> createTask(@RequestHeader("Authorization") String token,@Valid @RequestBody FAQRequestDTO faqRequest)
			throws MessagingException {
		faqService.saveFAQ(jwtTokenProvider.getUserIDFromToken(token.substring(7)),faqRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("FAQ Created Succesfully"));
	}
	
	
	@PutMapping("/{faqID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> updateTask(@RequestHeader("Authorization") String token,@PathVariable Long faqID,@Valid @RequestBody FAQRequestDTO faqRequest)
			throws MessagingException {
		faqService.updateFAQ(jwtTokenProvider.getUserIDFromToken(token.substring(7)),faqID,faqRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("FAQ Update Succesfully"));
	}
}
