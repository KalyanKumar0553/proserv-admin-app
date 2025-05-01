package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.FrequentlyAskedQuestion;


public interface FrequentlyAskedQuestionRepository extends JpaRepository<FrequentlyAskedQuestion, Long> {
	
	
	void deleteAllById(Long id);
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	void deleteAllByIdAndServiceTaskID(Long id,Long serviceTaskID);
	void deleteAllByServiceCategoryIDAndServiceTaskID(Long serviceCategoryID,Long serviceTaskID);
	
	
	List<FrequentlyAskedQuestion> findAllByServiceTaskID(Long taskID);
	List<FrequentlyAskedQuestion> findAllByServiceTaskIDAndServiceCategoryID(Long taskID,Long categoryID);
	Optional<FrequentlyAskedQuestion> findByIdAndServiceTaskID(Long ID, Long serviceTaskID);
	Optional<FrequentlyAskedQuestion> findByQuestionAndServiceTaskID(String question, Long serviceTaskID);
}
