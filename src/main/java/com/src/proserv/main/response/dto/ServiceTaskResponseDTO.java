
package com.src.proserv.main.response.dto;

import com.src.proserv.main.model.ServiceTask;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTaskResponseDTO {

	private Long id;

    private String title;

    private String serviceTitle;
    
    private String description;
    
    private Long serviceCategoryID;

    private String inclusions;

    private String exclusions;
    
    private String displayURL;
    
    private String note;

    public static ServiceTaskResponseDTO fromEntityToServiceTaskResponse(ServiceTask taskEntity) {
        return ServiceTaskResponseDTO.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
				.serviceCategoryID(taskEntity.getServiceCategoryID())
				.inclusions(taskEntity.getInclusions())
				.exclusions(taskEntity.getExclusions())
				.displayURL(taskEntity.getDisplayURL())
				.note(taskEntity.getNote())
				.build();
    }
}
