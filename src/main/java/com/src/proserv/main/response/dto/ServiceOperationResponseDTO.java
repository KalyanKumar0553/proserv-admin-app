
package com.src.proserv.main.response.dto;

import com.src.proserv.main.model.ServiceOperation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceOperationResponseDTO {

	private Long id;

    private String name;

    private Long serviceCategoryID;

    private String serviceTitle;

    private String displayURL;

    private String description;

    private Long options;

    public static ServiceOperationResponseDTO fromEntityToFetchOperationResponse(ServiceOperation operation) {
        return ServiceOperationResponseDTO.builder()
                .id(operation.getId())
                .name(operation.getName())
                .serviceTitle(operation.getServiceTitle())
                .description(operation.getDescription())
                .displayURL(operation.getDisplayURL())
                .build();
    }
}
