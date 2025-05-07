
package com.src.proserv.main.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.src.proserv.main.model.ServiceCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceCategoryResponseDTO {
	Long id;
	private String name;
    private String displayURL;
    private boolean enabled;
    private long providerCount;
    private long locationsCount;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ServiceTaskResponseDTO> serviceCategoryTasks;

    public static ServiceCategoryResponseDTO fromEntityToFetchCategoryResponse(ServiceCategory category) {
        return ServiceCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayURL(category.getDisplayURL())
                .enabled(category.isEnabled())
                .build();
    }
    
    public static ServiceCategoryResponseDTO fromEntityToFetchIndividualCategoryResponse(ServiceCategory category,List<ServiceTaskResponseDTO> serviceCategoryTasks) {
        return ServiceCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayURL(category.getDisplayURL())
                .enabled(category.isEnabled())
                .serviceCategoryTasks(serviceCategoryTasks)
                .build();
    }
}
