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
public class OperationService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceOperationRepository serviceOperationRepository;

	final ServiceOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	final ServiceOperationTransformer serviceOperationTransformer;

	public List<ServiceOperationResponseDTO> fetchAllServiceOperations(Long serviceCategoryID) {
		return serviceOperationRepository.findAllByServiceCategoryID(serviceCategoryID).stream()
				.map(ServiceOperationResponseDTO::fromEntityToFetchOperationResponse).collect(Collectors.toList());
	}

	public void deleteServiceOperation(Long operationID,Long serviceCategoryID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceCategoryIDAndServiceOperationID(serviceCategoryID, operationID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Tasks available to work for the operation");
		}
		Optional<ServiceOperation> currOperation = serviceOperationRepository.findByIdAndServiceCategoryID(operationID,serviceCategoryID);
		if(currOperation.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service operation with Given details not available");
		}
		optionRepository.deleteAllByServiceOperationIDAndServiceCategoryID(operationID,serviceCategoryID);
		taskRepository.deleteAllByServiceOperationIDAndServiceCategoryID(operationID,serviceCategoryID);
		faqRepository.deleteAllByServiceOperationIDAndServiceCategoryID(operationID,serviceCategoryID);
		serviceOperationRepository.deleteAllByIdAndServiceCategoryID(operationID,serviceCategoryID);
	}

	public String createServiceOperation(String userUUID, ServiceOperationRequestDTO serviceOperationRequestDTO) {
		ServiceOperation serviceOperation = ServiceOperationRequestDTO
				.toEntityFromOperationRequestDTO(serviceOperationRequestDTO);
		Optional<ServiceOperation> existingServiceOperation = serviceOperationRepository
				.findByServiceCategoryIDAndNameAndServiceTitle(serviceOperationRequestDTO.getServiceCategoryID(),
						serviceOperationRequestDTO.getName(), serviceOperationRequestDTO.getServiceTitle());
		if (existingServiceOperation.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Service Operation with Name and Title exists already");
		}
		serviceOperationRepository.save(serviceOperation);
		return "Service Operation Created Succesfully";
	}

	public String updateServiceOperation(String userUUID,ServiceOperationRequestDTO serviceOperationRequestDTO) {
		Optional<ServiceOperation> existingServiceOperation = serviceOperationRepository
				.findByIdAndServiceCategoryID(serviceOperationRequestDTO.getId(), serviceOperationRequestDTO.getServiceCategoryID());
		if (existingServiceOperation.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Service Operation with ID and Category ID not available");
		}
		ServiceOperation serviceOperation = existingServiceOperation.get();
		serviceOperation.setName(serviceOperationRequestDTO.getName());
		serviceOperation.setServiceTitle(serviceOperationRequestDTO.getServiceTitle());
		serviceOperation.setDescription(serviceOperationRequestDTO.getDescription());
		serviceOperation.setDisplayURL(serviceOperationRequestDTO.getDisplayURL());
		serviceOperation.setCreatedBy(userUUID);
		serviceOperation.setCreatedOn(LocalDateTime.now());
		serviceOperationRepository.save(serviceOperation);
		return "Service Operation Updated Succesfully";
	}
}
