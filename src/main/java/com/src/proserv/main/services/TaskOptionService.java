package com.src.proserv.main.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.ServiceTaskOption;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceTaskOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.ServiceTaskOptionRequestDTO;
import com.src.proserv.main.response.dto.ServiceTaskOptionResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskOptionService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	public List<ServiceTaskOptionResponseDTO> fetchAllServiceTaskOptions(Long serviceCategoryID,Long serviceTaskID) {
		return optionRepository.findAllByServiceCategoryIDAndServiceTaskID(serviceCategoryID,serviceTaskID)
				.stream().map(ServiceTaskOptionResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
	}

	public void deleteServiceOption(Long optionID,Long serviceCategoryID,Long taskID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceOptionID(optionID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Tasks available to work for the option");
		}
		Optional<ServiceTaskOption> currOption = optionRepository.findByIdAndServiceCategoryIDAndServiceTaskID(optionID,serviceCategoryID,taskID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service option with Given details not available");
		}
		optionRepository.deleteAllByIdAndServiceCategoryID(optionID,serviceCategoryID);
	}

	public void saveOption(String userIDFromToken, @Valid ServiceTaskOptionRequestDTO optionRequest) {
		
	}

	public void updateOption(String userIDFromToken, Long optionID, @Valid ServiceTaskOptionRequestDTO optionRequest) {
		
	}
}
