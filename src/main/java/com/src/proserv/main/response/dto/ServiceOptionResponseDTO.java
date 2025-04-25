
package com.src.proserv.main.response.dto;

import com.src.proserv.main.model.ServiceOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceOptionResponseDTO {

	private Long id;

    private String name;

    private String description;
    
    private String inclusions;
    
    private String exclusions;

	
    public static ServiceOptionResponseDTO fromEntityToFetchOptionResponse(ServiceOption option) {
        return ServiceOptionResponseDTO.builder()
                .id(option.getId())
                .name(option.getName())
                .description(option.getDescription())
                .inclusions(option.getInclusions())
            	.exclusions(option.getExclusions())
                .build();
    }
}
