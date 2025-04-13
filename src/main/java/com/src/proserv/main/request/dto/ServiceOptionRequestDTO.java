package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceOperation;
import com.src.proserv.main.model.ServiceOption;

import lombok.Data;

@Data
public class ServiceOptionRequestDTO {

	private Long id;

    private String name;

    private Long serviceOperationID;

    private String description;

    private Long serviceCategoryID;

	public static ServiceOption toEntityFromOperationRequestDTO(ServiceOptionRequestDTO requestDTO) {
		return ServiceOption.builder()
				.serviceCategoryID(requestDTO.getServiceCategoryID())
				.serviceOperationID(requestDTO.getServiceOperationID())
	            .name(requestDTO.getName())
	            .description(requestDTO.getDescription())
	            .build();
	}

}
