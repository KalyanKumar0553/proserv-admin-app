package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.src.proserv.main.model.FrequentlyAskedQuestion;


public interface FrequentlyAskedQuestionRepository extends JpaRepository<FrequentlyAskedQuestion, Long> {
	
	@Transactional
	void deleteAllById(Long id);
	@Transactional
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	@Transactional
	void deleteAllByIdAndServiceTaskID(Long id,Long serviceTaskID);
	@Transactional
	void deleteAllByServiceCategoryIDAndServiceTaskID(Long serviceCategoryID,Long serviceTaskID);
	
	
	List<FrequentlyAskedQuestion> findAllByServiceTaskID(Long taskID);
	List<FrequentlyAskedQuestion> findAllByServiceTaskIDAndServiceCategoryID(Long taskID,Long categoryID);
	Optional<FrequentlyAskedQuestion> findByIdAndServiceTaskID(Long ID, Long serviceTaskID);
	Optional<FrequentlyAskedQuestion> findByQuestionAndServiceTaskID(String question, Long serviceTaskID);
}
