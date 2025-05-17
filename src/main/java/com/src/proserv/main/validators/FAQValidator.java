package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class FAQValidator {

	final ServiceCategoryRepository categoryRepository;

	final ServiceTaskOptionRepository optionRepository;

	final ServiceTaskRepository taskRepository;

	final FrequentlyAskedQuestionRepository faqRepository;

	final UserServiceRequestTaskRepository userTaskRequestRepository;

	public void validateCreateFAQ(FAQRequestDTO faqRequest) {
		Optional<ServiceCategory> category = categoryRepository.findById(faqRequest.getServiceCategoryID());
		if(category.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Invalid Category Details");
		}
		Optional<ServiceTask> task = taskRepository.findByIdAndServiceCategoryID(faqRequest.getServiceTaskID(),faqRequest.getServiceCategoryID());
		if(task.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Invalid Task Details");
		}
		Optional<FrequentlyAskedQuestion> question = faqRepository.findByQuestionAndServiceTaskID(faqRequest.getQuestion(), faqRequest.getServiceTaskID());
		if(question.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Details for the task already available");
		}
	}
	
	public Optional<FrequentlyAskedQuestion> validateUpdateFAQ(Long questionID,FAQRequestDTO faqRequest) {
		Optional<ServiceCategory> category = categoryRepository.findById(faqRequest.getServiceCategoryID());
		if(category.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Invalid Category Details");
		}
		Optional<ServiceTask> task = taskRepository.findByIdAndServiceCategoryID(faqRequest.getServiceTaskID(),faqRequest.getServiceCategoryID());
		if(task.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Invalid Task Details");
		}
		Optional<FrequentlyAskedQuestion> question = faqRepository.findByQuestionAndServiceTaskID(faqRequest.getQuestion(), faqRequest.getServiceTaskID());
		if(question.isPresent() && !question.get().getId().equals(questionID)) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"FAQ With Given question for the task already available");
		}
		question = faqRepository.findByIdAndServiceCategoryIDAndServiceTaskID(questionID,faqRequest.getServiceCategoryID(),faqRequest.getServiceTaskID());
		if(!question.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"FAQ With Given ID not available");
		}
		return question;
	}
}
