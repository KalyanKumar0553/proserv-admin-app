package com.src.proserv.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.src.proserv.main.model.Provider;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.ProviderService;
import com.src.proserv.main.utils.AppUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value = "/api")
public class ServiceProviderController {

	final JWTTokenProvider tokenProvider;

	final ProviderService providerService;

	private final List<String> validGenders = List.of("M", "Male", "F", "Female");

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<Provider> createProvider(@RequestBody Provider provider,
			@RequestHeader("Authorization") String token) {
		if (provider.getName() == null || provider.getName().isBlank()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Provider Name cannot be Empty");
		}
		if (provider.getMobile() == null || provider.getMobile().isBlank()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Provider Mobile cannot be Empty");
		}
		if (provider.getGender() == null || provider.getGender().isBlank()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Provider Gender cannot be Empty");
		}
		if (!validGenders.contains(provider.getGender())) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(),
					"Invalid Gender. Only valid are " + validGenders);
		}
		return ResponseEntity.ok(providerService.saveProvider(provider, tokenProvider.getUserIDFromToken(token.substring(7))));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<List<Provider>> getAllProviders() {
		return ResponseEntity.ok(providerService.getAllProviders());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getProviderById(@PathVariable Long id) {
		return ResponseEntity.ok(AppUtils.getJSONObject(providerService.getProviderById(id)));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateProvider(@PathVariable Long id, @RequestBody Provider provider,
			@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(AppUtils.getJSONObject(
				providerService.updateProvider(id, provider, tokenProvider.getUserIDFromToken(token.substring(7)))));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteProvider(@PathVariable Long id) {
		providerService.deleteProvider(id);
		return ResponseEntity.ok(AppUtils.getJSONObject("Provider Deleted Succesfully"));
	}
}