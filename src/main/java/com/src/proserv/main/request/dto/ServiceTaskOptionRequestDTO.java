package com.src.proserv.main.request.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.src.proserv.main.model.ServiceTaskOption;

import lombok.Data;

@Data
public class ServiceTaskOptionRequestDTO {

	private Long id;

	@NotNull
	@NotEmpty
    private String name;

	@NotNull
	@NotEmpty
    private String description;

	@NotNull
	@NotEmpty
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
