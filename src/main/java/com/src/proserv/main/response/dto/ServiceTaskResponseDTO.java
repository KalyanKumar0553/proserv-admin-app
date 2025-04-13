
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

    private String name;

    private Long serviceOptionID;

    private Long serviceOperationID;

    private Long serviceCategoryID;

    private Long bookingAmount;

    private String inclusions;

    private String exclusions;

    public static ServiceTaskResponseDTO fromEntityToServiceTaskResponse(ServiceTask taskEntity) {
        return ServiceTaskResponseDTO.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
				.serviceOptionID(taskEntity.getServiceOptionID())
				.serviceOperationID(taskEntity.getServiceOperationID())
				.serviceCategoryID(taskEntity.getServiceCategoryID())
				.bookingAmount(taskEntity.getBookingAmount())
				.inclusions(taskEntity.getInclusions())
				.exclusions(taskEntity.getExclusions())
				.build();
    }
}
