package com.src.proserv.main.request.dto;

import javax.validation.constraints.NotNull;

import com.src.proserv.main.model.ServiceTask;

import lombok.Data;

@Data
public class ServiceTaskRequestDTO {

    private Long id;

    
    @NotNull
    private Long serviceCategoryID;

    @NotNull
    private String title;

    private String displayURL;

    private String description;
    
    private String inclusions;
    
    private String exclusions;
    
    private String note;
    
    private boolean enabled;

	public static ServiceTask toEntityFromTaskRequestDTO(ServiceTaskRequestDTO requestDTO) {
		return ServiceTask.builder()
				.title(requestDTO.getTitle())
				.serviceCategoryID(requestDTO.getServiceCategoryID())
				.displayURL(requestDTO.getDisplayURL())
				.description(requestDTO.getDescription())
				.inclusions(requestDTO.getInclusions())
				.exclusions(requestDTO.getExclusions())
				.enabled(requestDTO.isEnabled())
				.build();
	}

}
