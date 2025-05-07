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
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.FAQService;
import com.src.proserv.main.services.TaskOptionService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.validators.ServiceOptionValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/service/categories")
public class FAQController {


	final FAQService faqService;

	final JWTTokenProvider jwtTokenProvider;


	@DeleteMapping("/{serviceCategoryID}/tasks/{taskID}/faq/{faqID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> deleteOption(@RequestHeader("Authorization") String token,@PathVariable Long serviceCategoryID,@PathVariable Long taskID,@PathVariable Long faqID,Authentication authentication)
			throws MessagingException {
		faqService.deleteFAQ(faqID,serviceCategoryID,taskID);
		return ResponseEntity.ok(AppUtils.getJSONObject("Category Succesfully Deleted"));
	}


}
