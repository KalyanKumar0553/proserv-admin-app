package com.src.proserv.main.controllers;

import javax.mail.MessagingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.src.proserv.main.configuration.JWTTokenProvider;
import com.src.proserv.main.request.dto.DistrictRequestDTO;
import com.src.proserv.main.request.dto.StateRequestDTO;
import com.src.proserv.main.response.dto.JSONResponseDTO;
import com.src.proserv.main.services.ExcelImportService;
import com.src.proserv.main.services.GeographyService;
import com.src.proserv.main.utils.AppUtils;
import com.src.proserv.main.utils.RequestStatus;
import com.src.proserv.main.validators.GeographyValidator;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/geo")
public class GeographyController {
	
	final ExcelImportService excelImportService;
	
	final GeographyService geographyService;
	
	final GeographyValidator geographyValidator;
	
	final JWTTokenProvider jwtTokenProvider;
	
	
	@PostMapping("/upload")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<?> uploadCustomerExcel(@RequestHeader("Authorization") String token,@RequestParam MultipartFile file, @RequestParam String name, @RequestParam boolean override) {
        try {
            excelImportService.importExcel(jwtTokenProvider.getUserIDFromToken(token.substring(7)),file, name,override);
            return ResponseEntity.ok(AppUtils.getJSONObject(RequestStatus.UPLOAD_FILE_SUCCESS.getDescription()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RequestStatus.UPLOAD_FILE_FAILURE.getDescription() + e.getMessage());
        }
    }
	
	@GetMapping("/states")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getStates(Authentication authentication)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(geographyService.getStates()));
	}
	
	@PostMapping("/state")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createState(@RequestHeader("Authorization") String token,Authentication authentication,@RequestBody StateRequestDTO stateRequest)
			throws MessagingException {
        geographyValidator.validateStateRequestForCreate(stateRequest);
		geographyService.createState(jwtTokenProvider.getUserIDFromToken(token.substring(7)),stateRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("State Details Updated Correctly"));
	}
	
	
	@PutMapping("/state")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateState(@RequestHeader("Authorization") String token,Authentication authentication,@RequestBody StateRequestDTO stateRequest)
			throws MessagingException {
		geographyValidator.validateStateRequestForUpdate(stateRequest);
		geographyService.updateState(jwtTokenProvider.getUserIDFromToken(token.substring(7)),stateRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("State Details Updated Correctly"));
	}
	
	@DeleteMapping("/{stateCode}")
	public ResponseEntity<JSONResponseDTO<?>> deleteState(@PathVariable String stateCode,Authentication authentication) {
		geographyService.deleteState(stateCode);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Succesfully Deleted Address").isError(false).build());
    }
	
	
	@GetMapping("/{stateCode}/districts")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','USER')")
	public ResponseEntity<JSONResponseDTO<?>> getDistricts(Authentication authentication,@PathVariable String stateCode)
			throws MessagingException {
		return ResponseEntity.ok(AppUtils.getJSONObject(geographyService.getDistricts(stateCode)));
	}
	
	@PostMapping("/{stateCode}/district")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> createDistrict(@RequestHeader("Authorization") String token,Authentication authentication,@PathVariable String stateCode,@RequestBody DistrictRequestDTO districtRequest)
			throws MessagingException {
		districtRequest.setStateCode(stateCode);
		geographyValidator.validateDistrictRequestForCreate(districtRequest);
		geographyService.createDistrict(jwtTokenProvider.getUserIDFromToken(token.substring(7)),districtRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("State Details Updated Correctly"));
	}
	
	
	@PutMapping("/{stateCode}/district")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> updateDistrict(@RequestHeader("Authorization") String token,@PathVariable String stateCode,Authentication authentication,@RequestBody DistrictRequestDTO districtRequest)
			throws MessagingException {
		districtRequest.setStateCode(stateCode);
		geographyValidator.validateDistrictRequestForUpdate(districtRequest);
		geographyService.updateDistrict(jwtTokenProvider.getUserIDFromToken(token.substring(7)),districtRequest);
		return ResponseEntity.ok(AppUtils.getJSONObject("State Details Updated Correctly"));
	}
	
	@DeleteMapping("/{stateCode}/districts/{districtID}")
	@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<JSONResponseDTO<?>> deleteDistrict(@PathVariable String stateCode,@PathVariable Long districtID,Authentication authentication) {
		geographyValidator.validateDistrictRequestForDelete(stateCode,districtID);
		geographyService.deleteDistrict(stateCode,districtID);
		return ResponseEntity.ok(JSONResponseDTO.builder().statusMsg("User Succesfully Deleted Address").isError(false).build());
    }
}
