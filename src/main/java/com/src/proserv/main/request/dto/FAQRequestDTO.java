package com.src.proserv.main.request.dto;

import javax.persistence.Column;

import com.src.proserv.main.model.FrequentlyAskedQuestion;
import com.src.proserv.main.model.ServiceOption;
import com.src.proserv.main.model.ServiceTask;

import lombok.Data;

@Data
public class FAQRequestDTO {

	private Long id;

	private String question;

	private String answer;

	private Long serviceTaskID;

	private Long serviceCategoryID;

	private Long serviceOperationID;

	private Long serviceOptionID;

	public static FrequentlyAskedQuestion toEntityFromFAQRequestDTO(FAQRequestDTO faqRequest) {
		return FrequentlyAskedQuestion.builder()
				.question(faqRequest.getQuestion())
				.answer(faqRequest.getAnswer())
				.serviceTaskID(faqRequest.getServiceTaskID())
				.serviceCategoryID(faqRequest.getServiceCategoryID())
				.serviceOperationID(faqRequest.getServiceOperationID()).
				serviceOptionID(faqRequest.getServiceOptionID())
				.build();
	}
}
