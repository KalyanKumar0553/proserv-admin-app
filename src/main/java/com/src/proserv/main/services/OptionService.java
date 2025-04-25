package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.ServiceCategory;
import com.src.proserv.main.model.ServiceOperation;
import com.src.proserv.main.model.ServiceOption;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceOperationRepository;
import com.src.proserv.main.repository.ServiceOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.ServiceCategoryRequestDTO;
import com.src.proserv.main.request.dto.ServiceOperationRequestDTO;
import com.src.proserv.main.request.dto.ServiceOptionRequestDTO;
import com.src.proserv.main.response.dto.ServiceCategoryResponseDTO;
import com.src.proserv.main.response.dto.ServiceOperationResponseDTO;
import com.src.proserv.main.response.dto.ServiceOptionResponseDTO;
import com.src.proserv.main.transformers.ServiceOperationTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OptionService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceOperationRepository serviceOperationRepository;

	final ServiceOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	final ServiceOperationTransformer serviceOperationTransformer;

	public List<ServiceOptionResponseDTO> fetchAllServiceOptions(Long serviceCategoryID,Long serviceOperationID) {
		return optionRepository.findAllByServiceOperationIDAndServiceCategoryID(serviceCategoryID, serviceOperationID)
				.stream().map(ServiceOptionResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
	}

	public void deleteServiceOption(Long optionID,Long serviceCategoryID,Long serviceOperationID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceOptionID(optionID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Tasks available to work for the option");
		}
		Optional<ServiceOption> currOption = optionRepository.findByIdAndServiceOperationIDAndServiceCategoryID(optionID,serviceOperationID,serviceCategoryID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service option with Given details not available");
		}
		taskRepository.deleteAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(optionID,serviceOperationID,serviceCategoryID);
		faqRepository.deleteAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(optionID,serviceOperationID,serviceCategoryID);
		optionRepository.deleteAllByIdAndServiceOperationIDAndServiceCategoryID(optionID,serviceOperationID,serviceCategoryID);
	}

	public String createServiceOption(String userUUID,ServiceOptionRequestDTO serviceOptionRequest) {
		ServiceOption serviceOption = ServiceOptionRequestDTO.toEntityFromOperationRequestDTO(serviceOptionRequest);
		Optional<ServiceOption> existingServiceOption = optionRepository.findByNameAndServiceOperationIDAndServiceCategoryID(serviceOptionRequest.getName(),serviceOptionRequest.getServiceCategoryID(),serviceOptionRequest.getServiceCategoryID());
		if (existingServiceOption.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Operation with details already available");
		}
		serviceOption.setCreatedBy(userUUID);
		serviceOption.setCreatedOn(LocalDateTime.now());
		optionRepository.save(serviceOption);
		return "Service Option Created Succesfully";
	}

	public String updateServiceOption(String userUUID,ServiceOptionRequestDTO serviceOptionRequest) {
		Optional<ServiceOption> existingServiceOption = optionRepository.findByIdAndServiceOperationIDAndServiceCategoryID(serviceOptionRequest.getId(),serviceOptionRequest.getServiceOperationID(),serviceOptionRequest.getServiceCategoryID());
		if(existingServiceOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Option with Details not available");
		}
		ServiceOption serviceOption = existingServiceOption.get();
		serviceOption.setName(serviceOptionRequest.getName());
		serviceOption.setDescription(serviceOptionRequest.getDescription());
		serviceOption.setInclusions(serviceOptionRequest.getInclusions());
		serviceOption.setExclusions(serviceOptionRequest.getExclusions());
		optionRepository.save(serviceOption);
		return "Service Option Updated Succesfully";
	}

}
