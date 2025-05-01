package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceOptionValidator {

	public void validateCreateOptionRequest(ServiceTaskOptionRequestDTO optionRequest) {
		Optional<String> description = Optional.ofNullable(optionRequest.getDescription());
		Optional<String> name = Optional.ofNullable(optionRequest.getName());
		Optional<Long> categoryID = Optional.ofNullable(optionRequest.getServiceCategoryID());
		if (name.isEmpty() || name.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Option Name Cannot be Empty");
		}
		if (description.isEmpty() || description.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Option Description Cannot be Empty.");
		}
		if (categoryID.isEmpty() || categoryID.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Category ID Cannot be Empty.");
		}
	}

	public void validateUpdateOptionRequest(ServiceTaskOptionRequestDTO OptionRequest) {
		Optional<Long> id = Optional.ofNullable(OptionRequest.getId());
		if (id.isEmpty() || id.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Option ID.");
		}
		validateCreateOptionRequest(OptionRequest);
	}
}
