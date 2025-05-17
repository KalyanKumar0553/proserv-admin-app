package com.src.proserv.main.request.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.src.proserv.main.model.FrequentlyAskedQuestion;

import lombok.Data;

@Data
public class FAQRequestDTO {


	@NotNull
    @NotEmpty
    @Size(max = 256,message = "Answer must be at most 256 characters")
	private String question;

	@NotNull
    @NotEmpty
    @Size(max = 1024,message = "Answer must be at most 1024 characters")
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
