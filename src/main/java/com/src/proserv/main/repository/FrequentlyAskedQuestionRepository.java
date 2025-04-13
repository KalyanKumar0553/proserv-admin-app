package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.FrequentlyAskedQuestion;


public interface FrequentlyAskedQuestionRepository extends JpaRepository<FrequentlyAskedQuestion, Long> {
	void deleteAllByServiceOperationIDAndServiceCategoryID(Long serviceOptionID,Long serviceCategoryID);
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	void deleteAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long optionID,Long operationID,Long categoryID);
	void deleteAllByServiceTaskIDAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long taskID,Long optionID,Long operationID,Long categoryID);
	List<FrequentlyAskedQuestion> findAllByServiceTaskID(Long taskID);
	Optional<FrequentlyAskedQuestion> findByIdAndServiceTaskID(Long id,Long serviceTaskID);
	void deleteAllByIdAndServiceTaskID(Long id,Long serviceTaskID);
	Optional<FrequentlyAskedQuestion> findByQuestionAndServiceTaskID(String question, Long serviceTaskID);
}
