package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceTaskOption;

import lombok.Data;

@Data
public class ServiceTaskOptionRequestDTO {

	private Long id;

    private String name;

    private String description;

    private Long defaultAmount;

	public static ServiceTaskOption toEntityFromOperationRequestDTO(Long serviceCategoryID,Long serviceTaskID,ServiceTaskOptionRequestDTO requestDTO) {
		return ServiceTaskOption.builder()
				.name(requestDTO.getName())
	            .description(requestDTO.getDescription())
	            .serviceCategoryID(serviceCategoryID)
	            .serviceTaskID(serviceTaskID)
	            .defaultAmount(requestDTO.getDefaultAmount())
	            .build();
	}

}
