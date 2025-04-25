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
import com.src.proserv.main.repository.ServiceOperationRepository;
import com.src.proserv.main.repository.ServiceOptionRepository;
import com.src.proserv.main.repository.ServiceTaskRepository;
import com.src.proserv.main.repository.UserServiceRequestTaskRepository;
import com.src.proserv.main.request.dto.ServiceTaskRequestDTO;
import com.src.proserv.main.response.dto.ServiceTaskResponseDTO;
import com.src.proserv.main.transformers.ServiceOperationTransformer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskService {

	final ServiceCategoryRepository categoryRepository;

	final ServiceOperationRepository serviceOperationRepository;

	final ServiceOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	final ServiceOperationTransformer serviceOperationTransformer;

	public List<ServiceTaskResponseDTO> fetchAllServiceTasks(Long serviceCategoryID,Long serviceOperationID,Long serviceOptionID) {
		return taskRepository.findAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(serviceOptionID,serviceOperationID,serviceCategoryID)
				.stream().map(ServiceTaskResponseDTO::fromEntityToServiceTaskResponse).collect(Collectors.toList());
	}


	public void deleteServiceTask(Long taskID,Long serviceCategoryID,Long serviceOperationID,Long serviceOptionID) {
		long count = userTaskRequestRepository.countIncompleteTasksByServiceTaskID(taskID);
		if (count > 0) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Pending Requests available to work for the task");
		}
		Optional<ServiceTask> currOption = taskRepository.findByIdAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(taskID,serviceOptionID,serviceOperationID,serviceCategoryID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service task with Given details not available");
		}
		faqRepository.deleteAllByServiceTaskIDAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(taskID,serviceOptionID,serviceOperationID,serviceCategoryID);
		taskRepository.deleteAllByIdAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(taskID,serviceOptionID,serviceOperationID,serviceCategoryID);
	}

	public String createServiceTask(String userUUID,ServiceTaskRequestDTO serviceTaskRequest) {
		String name = serviceTaskRequest.getName();
		Long serviceOptionID = serviceTaskRequest.getServiceOptionID();
		Long serviceCategoryID = serviceTaskRequest.getServiceCategoryID();
		Long serviceOperationID = serviceTaskRequest.getServiceOperationID();
		ServiceTask serviceTask = ServiceTaskRequestDTO.toEntityFromTaskRequestDTO(serviceTaskRequest);
		Optional<ServiceTask> currOption = taskRepository.findByNameAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(name,serviceOptionID,serviceOperationID,serviceCategoryID);
		if(currOption.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service task with Given details not available");
		}
		serviceTask.setCreatedBy(userUUID);
		serviceTask.setCreatedOn(LocalDateTime.now());
		taskRepository.save(serviceTask);
		return "Service Task Created Succesfully";
	}

	public String updateServiceTask(String userUUID,ServiceTaskRequestDTO serviceTaskRequest) {
		Long serviceOptionID = serviceTaskRequest.getServiceOptionID();
		Long serviceCategoryID = serviceTaskRequest.getServiceCategoryID();
		Long serviceOperationID = serviceTaskRequest.getServiceOperationID();
		Optional<ServiceTask> existingServiceTask = taskRepository.findByIdAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(serviceTaskRequest.getId(),serviceOptionID,serviceOperationID,serviceCategoryID);
		if(existingServiceTask.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Service Task with Details not available");
		}
		ServiceTask serviceTask = existingServiceTask.get();
		serviceTask.setName(serviceTaskRequest.getName());
		serviceTask.setServiceOptionID(serviceTaskRequest.getServiceOptionID());
		serviceTask.setServiceOperationID(serviceTaskRequest.getServiceOperationID());
		serviceTask.setServiceCategoryID(serviceTaskRequest.getServiceCategoryID());
		serviceTask.setBookingAmount(serviceTaskRequest.getBookingAmount());
		taskRepository.save(serviceTask);
		return "Service Task Updated Succesfully";
	}

}
