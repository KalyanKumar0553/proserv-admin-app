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

	public void deleteFAQ(Long categoryID,Long taskID,Long faqID) {
		Optional<FrequentlyAskedQuestion> currFAQ = faqRepository.findByIdAndServiceCategoryIDAndServiceTaskID(faqID,categoryID,taskID);
		if (currFAQ.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"FAQ With Given details not available");
		}
		faqRepository.deleteAllByIdAndServiceCategoryIDAndServiceTaskID(faqID, categoryID,taskID);
	}
}
