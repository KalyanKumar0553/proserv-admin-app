package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceOption;

import lombok.Data;

@Data
public class ServiceOptionRequestDTO {

	private Long id;

    private String name;

    private Long serviceOperationID;

    private String description;

    private Long serviceCategoryID;
    
    private String inclusions;

    private String exclusions;


	public static ServiceOption toEntityFromOperationRequestDTO(ServiceOptionRequestDTO requestDTO) {
		return ServiceOption.builder()
				.serviceCategoryID(requestDTO.getServiceCategoryID())
				.serviceOperationID(requestDTO.getServiceOperationID())
	            .name(requestDTO.getName())
	            .description(requestDTO.getDescription())
	            .inclusions(requestDTO.getInclusions())
	            .exclusions(requestDTO.getExclusions())
	            .build();
	}

}
