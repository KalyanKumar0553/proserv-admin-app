package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.ServiceTask;
import com.src.proserv.main.repository.FrequentlyAskedQuestionRepository;
import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.repository.ServiceTaskOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;
import com.src.proserv.main.response.dto.FAQResponseDTO;
import com.src.proserv.main.response.dto.ServiceTaskOptionResponseDTO;
import com.src.proserv.main.response.dto.ServiceTaskResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;
	
	final TaskOptionService optionService;

	public List<ServiceTaskResponseDTO> fetchAllServiceTasks(Long serviceCategoryID) {
		return taskRepository.findAllByServiceCategoryID(serviceCategoryID)
				.stream().map(ServiceTaskResponseDTO::fromEntityToAllServiceTaskResponse).collect(Collectors.toList());
	}
	
	public List<ServiceTaskResponseDTO> fetchServiceTaskByID(Long serviceCategoryID,Long serviceTaskID) {
		List<FAQResponseDTO> faqs = faqRepository.findAllByServiceTaskIDAndServiceCategoryID(serviceTaskID, serviceCategoryID).stream().map(FAQResponseDTO::fromEntityToFetchOptionResponse).collect(Collectors.toList());
		List<ServiceTaskOptionResponseDTO> taskOptions = optionService.fetchAllServiceTaskOptions(serviceCategoryID, serviceTaskID);
		return taskRepository.findByIdAndServiceCategoryID(serviceTaskID,serviceCategoryID)
				.stream().map(e->ServiceTaskResponseDTO.fromEntityToIndiviualServiceTaskResponse(e,faqs,taskOptions)).collect(Collectors.toList());
	}


	public void deleteServiceTask(Long serviceCategoryID,Long serviceTaskID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceTaskID(serviceTaskID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Pending Requests available to work for the task");
		}
		Optional<ServiceTask> currOption = taskRepository.findByIdAndServiceCategoryID(serviceTaskID,serviceCategoryID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service task with Given details not available");
		}
		faqRepository.deleteAllByServiceCategoryIDAndServiceTaskID(serviceCategoryID,serviceTaskID);
		optionRepository.deleteAllByServiceCategoryIDAndServiceTaskID(serviceCategoryID,serviceTaskID);
		taskRepository.deleteAllByIdAndServiceCategoryID(serviceTaskID,serviceCategoryID);
	}

	public String createServiceTask(String userUUID,ServiceTaskRequestDTO serviceTaskRequest) {
		String title = serviceTaskRequest.getTitle();
		Long serviceCategoryID = serviceTaskRequest.getServiceCategoryID();
		ServiceTask serviceTask = ServiceTaskRequestDTO.toEntityFromTaskRequestDTO(serviceTaskRequest);
		Optional<ServiceTask> currOption = taskRepository.findByTitleAndServiceCategoryIDIgnoreCase(title,serviceCategoryID);
		if(currOption.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service task with Given Title already available");
		}
		serviceTask.setCreatedBy(userUUID);
		serviceTask.setCreatedOn(LocalDateTime.now());
		serviceTask.setLastModifiedBy(userUUID);
		serviceTask.setLastModifiedOn(LocalDateTime.now());
		taskRepository.save(serviceTask);
		return "Service Task Created Succesfully";
	}

	public String updateServiceTask(String userUUID,ServiceTaskRequestDTO serviceTaskRequest) {
		Long serviceCategoryID = serviceTaskRequest.getServiceCategoryID();
		Optional<ServiceTask> existingServiceTask = taskRepository.findByIdAndServiceCategoryID(serviceTaskRequest.getId(),serviceCategoryID);
		if(existingServiceTask.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Task with Details not available");
		}
		ServiceTask serviceTask = existingServiceTask.get();
		serviceTask.setTitle(serviceTaskRequest.getTitle());
		serviceTask.setServiceCategoryID(serviceTaskRequest.getServiceCategoryID());
		taskRepository.save(serviceTask);
		return "Service Task Updated Succesfully";
	}

}
