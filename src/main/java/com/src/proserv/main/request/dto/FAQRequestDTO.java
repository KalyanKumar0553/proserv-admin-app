package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.FrequentlyAskedQuestion;

import lombok.Data;

@Data
public class FAQRequestDTO {


    private String question;

    private String answer;

    private Long serviceTaskID;

    private Long serviceCategoryID;

	public static FrequentlyAskedQuestion toEntityFromFAQRequestDTO(FAQRequestDTO faqRequest) {
		return FrequentlyAskedQuestion.builder()
				.question(faqRequest.getQuestion())
				.answer(faqRequest.getAnswer())
				.serviceTaskID(faqRequest.getServiceTaskID())
				.serviceCategoryID(faqRequest.getServiceCategoryID())
				.build();
	}
}
