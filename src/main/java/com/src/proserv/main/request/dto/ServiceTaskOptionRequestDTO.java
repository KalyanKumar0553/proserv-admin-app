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
    private String description;

	@NotNull
    private Long serviceCategoryID;

	@NotNull
    private Long serviceTaskID;
    
    private Long defaultAmount;
    
    private Long taskDuration;
    
    private String inclusions;
    
    private String exclusions;
    
    private String displayURL;

	public static ServiceTaskOption toEntityFromOperationRequestDTO(ServiceTaskOptionRequestDTO requestDTO) {
		return ServiceTaskOption.builder()
				.name(requestDTO.getName())
	            .description(requestDTO.getDescription())
	            .serviceCategoryID(requestDTO.getServiceCategoryID())
	            .serviceTaskID(requestDTO.getServiceTaskID())
	            .defaultAmount(requestDTO.getDefaultAmount())
	            .taskDuration(requestDTO.getTaskDuration())
	            .displayURL(requestDTO.getDisplayURL())
	            .inclusions(requestDTO.getInclusions())
	            .exclusions(requestDTO.getExclusions())
	            .build();
	}

}
