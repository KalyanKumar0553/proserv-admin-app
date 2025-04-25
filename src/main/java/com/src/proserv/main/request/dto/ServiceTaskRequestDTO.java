package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceOption;
import com.src.proserv.main.model.ServiceTask;

import lombok.Data;

@Data
public class ServiceTaskRequestDTO {

    private Long id;

    private String name;

    private Long serviceOptionID;

    private Long serviceOperationID;

    private Long serviceCategoryID;

    private Long bookingAmount;

    private String displayURL;

	public static ServiceTask toEntityFromTaskRequestDTO(ServiceTaskRequestDTO requestDTO) {
		return ServiceTask.builder()
				.name(requestDTO.getName())
				.serviceOptionID(requestDTO.getServiceOptionID())
				.serviceOperationID(requestDTO.getServiceOperationID())
				.serviceCategoryID(requestDTO.getServiceCategoryID())
				.bookingAmount(requestDTO.getBookingAmount())
				.build();
	}

}
