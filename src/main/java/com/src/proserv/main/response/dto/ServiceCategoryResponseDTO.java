
package com.src.proserv.main.response.dto;

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


    public static ServiceCategoryResponseDTO fromEntityToFetchCategoryResponse(ServiceCategory category) {
        return ServiceCategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .displayURL(category.getDisplayURL())
                .build();
    }
}
