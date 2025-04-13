
package com.src.proserv.main.response.dto;

import javax.persistence.Column;

import com.src.proserv.main.model.FrequentlyAskedQuestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FAQResponseDTO {

	private Long id;

    private String question;

    private String answer;

    public static FAQResponseDTO fromEntityToFetchOptionResponse(FrequentlyAskedQuestion option) {
        return FAQResponseDTO.builder()
                .id(option.getId())
                .question(option.getQuestion())
                .answer(option.getAnswer())
                .build();
    }
}
