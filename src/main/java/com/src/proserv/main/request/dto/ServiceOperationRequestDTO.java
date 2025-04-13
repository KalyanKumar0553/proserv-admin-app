package com.src.proserv.main.request.dto;

import java.util.Set;

import com.src.proserv.main.model.ServiceOperation;

import lombok.Data;

@Data
public class ServiceOperationRequestDTO {

	private Long id;

    private String name;

    private Long serviceCategoryID;

    private String serviceTitle;

    private String displayURL;

    private String description;

    private Set<Long> options;

	public static ServiceOperation toEntityFromOperationRequestDTO(ServiceOperationRequestDTO requestDTO) {
        return ServiceOperation.builder()
                .name(requestDTO.getName())
                .serviceTitle(requestDTO.getServiceTitle())
                .description(requestDTO.getDescription())
                .displayURL(requestDTO.getDisplayURL())
                .build();
    }
}
