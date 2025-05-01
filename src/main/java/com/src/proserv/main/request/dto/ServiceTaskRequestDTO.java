package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.ServiceTask;

import lombok.Data;

@Data
public class ServiceTaskRequestDTO {

    private Long id;

    private String name;
    
    private Long serviceCategoryID;

    private String title;

    private String displayURL;

    private String description;
    
    private String inclusions;
    
    private String exclusions;
    
    private String note;

	public static ServiceTask toEntityFromTaskRequestDTO(ServiceTaskRequestDTO requestDTO) {
		return ServiceTask.builder()
				.title(requestDTO.getTitle())
				.serviceCategoryID(requestDTO.getServiceCategoryID())
				.displayURL(requestDTO.getDisplayURL())
				.description(requestDTO.getDescription())
				.inclusions(requestDTO.getInclusions())
				.exclusions(requestDTO.getExclusions())
				.build();
	}

}
