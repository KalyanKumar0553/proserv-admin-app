
package com.src.proserv.main.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
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

	private String description;

	private Long serviceCategoryID;

	private String inclusions;

	private String exclusions;

	private String displayURL;

	private String note;

	private boolean enabled;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<FAQResponseDTO> faqs;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	List<ServiceTaskOptionResponseDTO> options;

	public static ServiceTaskResponseDTO fromEntityToAllServiceTaskResponse(ServiceTask taskEntity) {
		return ServiceTaskResponseDTO.builder().id(taskEntity.getId()).title(taskEntity.getTitle())
				.description(taskEntity.getDescription()).serviceCategoryID(taskEntity.getServiceCategoryID())
				.inclusions(taskEntity.getInclusions()).exclusions(taskEntity.getExclusions())
				.displayURL(taskEntity.getDisplayURL()).note(taskEntity.getNote()).enabled(taskEntity.isEnabled())
				.build();
	}

	public static ServiceTaskResponseDTO fromEntityToIndiviualServiceTaskResponse(ServiceTask taskEntity,
			List<FAQResponseDTO> faqResponse, List<ServiceTaskOptionResponseDTO> taskOptions) {
		return ServiceTaskResponseDTO.builder().id(taskEntity.getId()).title(taskEntity.getTitle())
				.description(taskEntity.getDescription()).serviceCategoryID(taskEntity.getServiceCategoryID())
				.inclusions(taskEntity.getInclusions()).exclusions(taskEntity.getExclusions())
				.displayURL(taskEntity.getDisplayURL()).faqs(faqResponse).note(taskEntity.getNote())
				.enabled(taskEntity.isEnabled()).options(taskOptions).build();
	}
}
