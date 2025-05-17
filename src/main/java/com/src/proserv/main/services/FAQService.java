package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.FrequentlyAskedQuestion;
import com.src.proserv.main.model.ServiceCategory;
import com.src.proserv.main.model.ServiceTask;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceTaskOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.FAQRequestDTO;
import com.src.proserv.main.response.dto.FAQResponseDTO;
import com.src.proserv.main.validators.FAQValidator;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FAQService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;
	
	final FAQValidator faqValidator;

	public List<FAQResponseDTO> fetchAllFAQ(Long taskID) {
		return faqRepository.findAllByServiceTaskID(taskID).stream().map(FAQResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
	}

	public void deleteFAQ(Long faqID) {
		Optional<FrequentlyAskedQuestion> currFAQ = faqRepository.findById(faqID);
		if (currFAQ.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"FAQ With Given details not available");
		}
		faqRepository.deleteAllById(faqID);
	}
	
	public void saveFAQ(String string, FAQRequestDTO faqRequest) {
		faqValidator.validateCreateFAQ(faqRequest);
		FrequentlyAskedQuestion faqEntity = FAQRequestDTO.toEntityFromFAQRequestDTO(faqRequest);
		faqRepository.save(faqEntity);
	}
	
	public void updateFAQ(String userID, Long questionID,FAQRequestDTO faqRequest) {
		var question = faqValidator.validateUpdateFAQ(questionID,faqRequest);
		FrequentlyAskedQuestion faqEntity = question.get();
		faqEntity.setLastModifiedBy(userID);
		faqEntity.setLastModifiedOn(LocalDateTime.now());
		if(faqRequest.getQuestion()!=null && !faqRequest.getQuestion().equals(faqEntity.getQuestion())) {
			faqEntity.setQuestion(faqRequest.getQuestion());
		}
		if(faqRequest.getAnswer()!=null && !faqRequest.getAnswer().equals(faqEntity.getAnswer())) {
			faqEntity.setAnswer(faqRequest.getAnswer());
		}
		faqRepository.save(faqEntity);
	}
}
