package com.src.proserv.main.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.ServiceCategory;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceTaskOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.ServiceCategoryRequestDTO;
import com.src.proserv.main.response.dto.ServiceCategoryResponseDTO;
import com.src.proserv.main.response.dto.ServiceTaskResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoryService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;
	
	final TaskService taskService;

	public List<ServiceCategoryResponseDTO> fetchAllServiceCategories() {
		return categoryRepository.findAll().stream().map(ServiceCategoryResponseDTO::fromEntityToFetchCategoryResponse)
				.collect(Collectors.toList());
	}
	
	public List<ServiceCategoryResponseDTO> fetchServiceCategoryByID(Long serviceCategoryID) {
		List<ServiceTaskResponseDTO> serviceCategoryTasks = taskService.fetchAllServiceTasks(serviceCategoryID);
		return categoryRepository.findById(serviceCategoryID).stream()
				.map(c->ServiceCategoryResponseDTO.fromEntityToFetchIndividualCategoryResponse(c,serviceCategoryTasks))
				.collect(Collectors.toList());
	}

	public void deleteServiceCategory(Long serviceCategoryID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceCategoryID(serviceCategoryID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Tasks available to work for the service category");
		}
		optionRepository.deleteAllByServiceCategoryID(serviceCategoryID);
		taskRepository.deleteAllByServiceCategoryID(serviceCategoryID);
		faqRepository.deleteAllByServiceCategoryID(serviceCategoryID);
		categoryRepository.deleteById(serviceCategoryID);
	}

	public String createServiceCategory(String userUUID, ServiceCategoryRequestDTO serviceCategoryRequest) {
		ServiceCategory serviceCategory = ServiceCategory.builder().name(serviceCategoryRequest.getName())
				.displayURL(serviceCategoryRequest.getDisplayURL())
				.enabled(serviceCategoryRequest.isEnabled())
				.build();
		Optional<ServiceCategory> existingServiceCategory = categoryRepository
				.findByNameIgnoreCase(serviceCategoryRequest.getName());
		if (existingServiceCategory.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Service Category with Name exists already");
		}
		categoryRepository.save(serviceCategory);
		return "Service Category Created Succesfully";
	}

	public String updateServiceCategory(String userUUID, ServiceCategoryRequestDTO serviceCategoryRequest) {
		Optional<ServiceCategory> existingServiceCategory = categoryRepository.findByIdAndName(serviceCategoryRequest.getId(),serviceCategoryRequest.getName());
		if (existingServiceCategory.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Service Category with Name and ID not available");
		}
		ServiceCategory sc = existingServiceCategory.get();
		sc.setName(serviceCategoryRequest.getName());
		sc.setDisplayURL(serviceCategoryRequest.getDisplayURL());
		sc.setEnabled(serviceCategoryRequest.isEnabled());
		categoryRepository.save(sc);
		return "Service Category Created Succesfully";
	}
}
