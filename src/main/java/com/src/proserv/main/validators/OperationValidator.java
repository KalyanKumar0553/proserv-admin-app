package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.request.dto.ServiceCategoryRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OperationValidator {
	
	public void validateCreateCategoryRequest(ServiceCategoryRequestDTO categoryRequest) {
		Optional<String> name = Optional.ofNullable(categoryRequest.getName());
		Optional<String> displayURL = Optional.ofNullable(categoryRequest.getDisplayURL());
		if (name.isEmpty() || name.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Category Name Cannot be Empty");
		}
		if (displayURL.isEmpty() || displayURL.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Request.");
		}
	}

	public void validateUpdateCategoryRequest(ServiceCategoryRequestDTO categoryRequestDTO) {
		validateCreateCategoryRequest(categoryRequestDTO);
	}
}
