package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.request.dto.ServiceOptionRequestDTO;
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ServiceTaskValidator {

	public void validateCreateTaskRequest(ServiceTaskRequestDTO taskRequest) {
		Optional<String> name = Optional.ofNullable(taskRequest.getName());
		Optional<Long> bookingAmount = Optional.ofNullable(taskRequest.getBookingAmount());
		Optional<String> inclusions = Optional.ofNullable(taskRequest.getInclusions());
		Optional<String> exclusions = Optional.ofNullable(taskRequest.getExclusions());
		Optional<Long> serviceOptionID = Optional.ofNullable(taskRequest.getServiceOptionID());
		Optional<Long> serviceOperationID = Optional.ofNullable(taskRequest.getServiceOperationID());
		Optional<Long> serviceCategoryID = Optional.ofNullable(taskRequest.getServiceCategoryID());
		if (name.isEmpty() || name.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Option Name Cannot be Empty");
		}
		if (bookingAmount.isEmpty() || bookingAmount.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Booking amount");
		}
		if (inclusions.isEmpty() || inclusions.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Inclusion Details are Mandatory");
		}
		if (exclusions.isEmpty() || exclusions.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Exclusion Details are Mandatory");
		}
		if (serviceOptionID.isEmpty() || serviceOptionID.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Option ID");
		}
		if (serviceOperationID.isEmpty() || serviceOperationID.get()<=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid Operation ID");
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
