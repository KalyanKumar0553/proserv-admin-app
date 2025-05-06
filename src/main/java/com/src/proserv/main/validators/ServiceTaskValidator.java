package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceTaskValidator {

	public void validateCreateTaskRequest(ServiceTaskRequestDTO taskRequest) {
		Optional<String> title = Optional.ofNullable(taskRequest.getTitle());
		Optional<Long> serviceCategoryID = Optional.ofNullable(taskRequest.getServiceCategoryID());
		if (title.isEmpty() || title.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Option Name Cannot be Empty");
		}
		if (serviceCategoryID.isEmpty() || serviceCategoryID.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Category ID");
		}

	}

	public void validateUpdateTaskRequest(ServiceTaskRequestDTO OptionRequest) {
		Optional<Long> id = Optional.ofNullable(OptionRequest.getId());
		if (id.isEmpty() || id.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Option ID.");
		}
		validateCreateTaskRequest(OptionRequest);
	}
}
