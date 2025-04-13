package com.src.proserv.main.controllers;

import java.util.List;

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
import com.src.proserv.main.model.OnboardedLocation;
import com.src.proserv.main.model.Provider;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.OnboardedLocationService;
import com.src.proserv.main.utils.AppUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(value = "/api/onboard")
public class OnboardedLocationController {
	
	final JWTTokenProvider tokenProvider;
	
	final OnboardedLocationService onboardedLocationService;

	@PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<JSONResponseDTO<?>> createOnboardedLocation(@RequestBody OnboardedLocation onboardedLocation,@RequestHeader("Authorization") String token) {
    	if(onboardedLocation.getStateID()==null || onboardedLocation.getStateID()<=0) {
        	throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Provider Name cannot be Empty");
        }
    	if(onboardedLocation.getDistrictID()==null || onboardedLocation.getDistrictID()<=0) {
        	throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Provider Mobile cannot be Empty");
        }
    	if(onboardedLocation.getName()==null || onboardedLocation.getName().isBlank()) {
        	throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Location Name cannot be Empty");
        }
    	return ResponseEntity.ok(AppUtils.getJSONObject(onboardedLocationService.save(onboardedLocation,tokenProvider.getUserIDFromToken(token.substring(7)))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
    public ResponseEntity<JSONResponseDTO<?>> getAllOnboardedLocation() {
    	return ResponseEntity.ok(AppUtils.getJSONObject(onboardedLocationService.getAllLocations()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
    public ResponseEntity<JSONResponseDTO<?>> getOnboardedLocationById(@PathVariable Long id) {
    	return ResponseEntity.ok(AppUtils.getJSONObject(onboardedLocationService.findById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<JSONResponseDTO<?>> updateProvider(@PathVariable Long id, @RequestBody OnboardedLocation location,@RequestHeader("Authorization") String token) {
    	return ResponseEntity.ok(AppUtils.getJSONObject(onboardedLocationService.update(id, location,tokenProvider.getUserIDFromToken(token.substring(7)))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ResponseEntity<JSONResponseDTO<?>> deleteProvider(@PathVariable Long id) {
    	onboardedLocationService.delete(id);
        return ResponseEntity.ok(AppUtils.getJSONObject("Provider Deleted Succesfully"));
    }
}