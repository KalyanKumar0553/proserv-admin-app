package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.request.dto.ServiceCategoryRequestDTO;
import com.src.proserv.main.request.dto.ServiceOperationRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceOperationValidator {

	public void validateCreateOperationRequest(ServiceOperationRequestDTO operationRequest) {
		Optional<String> description = Optional.ofNullable(operationRequest.getDescription());
		Optional<String> name = Optional.ofNullable(operationRequest.getName());
		Optional<String> title = Optional.ofNullable(operationRequest.getServiceTitle());
		if (name.isEmpty() || name.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Operation Name Cannot be Empty");
		}
		if (title.isEmpty() || title.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Operation Title Cannot be Empty.");
		}
		if (description.isEmpty() || description.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Operation Description Cannot be Empty.");
		}
	}

	public void validateUpdateOperationRequest(ServiceOperationRequestDTO operationRequest) {
		Optional<Long> id = Optional.ofNullable(operationRequest.getId());
		if (id.isEmpty() || id.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Operation ID Cannot be Empty.");
		}
		validateCreateOperationRequest(operationRequest);
	}
}
