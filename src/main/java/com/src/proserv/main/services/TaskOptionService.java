package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.src.proserv.main.response.dto.ServiceOptionResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskOptionService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	public List<ServiceOptionResponseDTO> fetchAllServiceOptions(Long serviceCategoryID) {
		return optionRepository.findAllByServiceCategoryID(serviceCategoryID)
				.stream().map(ServiceOptionResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
	}

	public void deleteServiceOption(Long optionID,Long serviceCategoryID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceOptionID(optionID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Tasks available to work for the option");
		}
		Optional<ServiceTaskOption> currOption = optionRepository.findByIdAndServiceCategoryID(optionID,serviceCategoryID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service option with Given details not available");
		}
		taskRepository.deleteAllByServiceCategoryID(serviceCategoryID);
		faqRepository.deleteAllByServiceCategoryID(serviceCategoryID);
		optionRepository.deleteAllByIdAndServiceCategoryID(optionID,serviceCategoryID);
	}

	public String createServiceOption(String userUUID,ServiceTaskOptionRequestDTO serviceOptionRequest) {
		ServiceTaskOption serviceOption = ServiceTaskOptionRequestDTO.toEntityFromOperationRequestDTO(serviceOptionRequest);
		Optional<ServiceTaskOption> existingServiceOption = optionRepository.findByNameAndServiceCategoryID(serviceOptionRequest.getName(),serviceOptionRequest.getServiceCategoryID());
		if (existingServiceOption.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Operation with details already available");
		}
		serviceOption.setCreatedBy(userUUID);
		serviceOption.setCreatedOn(LocalDateTime.now());
		optionRepository.save(serviceOption);
		return "Service Option Created Succesfully";
	}

	public String updateServiceOption(String userUUID,ServiceTaskOptionRequestDTO serviceOptionRequest) {
		Optional<ServiceTaskOption> existingServiceOption = optionRepository.findByIdAndServiceCategoryID(serviceOptionRequest.getId(),serviceOptionRequest.getServiceCategoryID());
		if(existingServiceOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Option with Details not available");
		}
		ServiceTaskOption serviceOption = existingServiceOption.get();
		serviceOption.setName(serviceOptionRequest.getName());
		serviceOption.setDescription(serviceOptionRequest.getDescription());
		optionRepository.save(serviceOption);
		return "Service Option Updated Succesfully";
	}

}
