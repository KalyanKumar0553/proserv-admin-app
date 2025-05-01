package com.src.proserv.main.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.FrequentlyAskedQuestion;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceTaskOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.FAQRequestDTO;
import com.src.proserv.main.response.dto.FAQResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FAQService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	public List<FAQResponseDTO> fetchAllFAQ(Long taskID) {
		return faqRepository.findAllByServiceTaskID(taskID).stream()
				.map(FAQResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
	}

	public void deleteFAQ(Long taskID, Long faqID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceTaskID(taskID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Tasks available to work for the faq assigned");
		}
		Optional<FrequentlyAskedQuestion> currFAQ = faqRepository.findByIdAndServiceTaskID(taskID, faqID);
		if (currFAQ.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"FAQ With Given details not available");
		}
		faqRepository.deleteAllByIdAndServiceTaskID(faqID, taskID);
	}

	public String createFAQ(String userUUID, FAQRequestDTO faqRequest) {
		FrequentlyAskedQuestion faq = FAQRequestDTO.toEntityFromFAQRequestDTO(faqRequest);
		Optional<FrequentlyAskedQuestion> existinFAQHolder = faqRepository
				.findByQuestionAndServiceTaskID(faqRequest.getQuestion(), faqRequest.getServiceTaskID());
		if (existinFAQHolder.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Question is already available with taskID");
		}
		faqRepository.save(faq);
		return "FAQ Created Succesfully";
	}

	public String updateFAQ(String userUUID, Long faqID, FAQRequestDTO faqRequest) {
		Optional<FrequentlyAskedQuestion> existinFAQHolder = faqRepository.findByIdAndServiceTaskID(faqID,
				faqRequest.getServiceTaskID());
		if (existinFAQHolder.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Question with given Details not available");
		}
		FrequentlyAskedQuestion existinFAQ = existinFAQHolder.get();
		existinFAQ.setQuestion(faqRequest.getQuestion());
		existinFAQ.setAnswer(faqRequest.getAnswer());
		faqRepository.save(existinFAQ);
		return "Service Question Updated Succesfully";
	}

}
