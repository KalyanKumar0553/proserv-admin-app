package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceTaskOption;

import lombok.Data;

@Data
public class ServiceTaskOptionRequestDTO {

	private Long id;

    private String name;

    private String description;

    private Long serviceCategoryID;
    
    private Long serviceTaskID;
    
    private Long defaultAmount;

	public static ServiceTaskOption toEntityFromOperationRequestDTO(ServiceTaskOptionRequestDTO requestDTO) {
		return ServiceTaskOption.builder()
				.name(requestDTO.getName())
	            .description(requestDTO.getDescription())
	            .serviceCategoryID(requestDTO.getServiceCategoryID())
	            .serviceTaskID(requestDTO.getServiceTaskID())
	            .defaultAmount(requestDTO.getDefaultAmount())
	            .build();
	}

}
