
package com.src.proserv.main.response.dto;

import com.src.proserv.main.model.ServiceTaskOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTaskOptionResponseDTO {

	private Long id;

    private String name;

    private String description;

    private Long serviceCategoryID;
    
    private Long serviceTaskID;
    
    private Long defaultAmount;

	
    public static ServiceTaskOptionResponseDTO fromEntityToFetchOptionResponse(ServiceTaskOption option) {
        return ServiceTaskOptionResponseDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .description(option.getDescription())
                .serviceCategoryID(option.getServiceCategoryID())
                .serviceTaskID(option.getServiceTaskID())
                .defaultAmount(option.getDefaultAmount())
                .build();
    }
}
