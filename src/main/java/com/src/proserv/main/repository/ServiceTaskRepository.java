package com.src.proserv.main.repository;


import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceTask;


public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long> {
	@Transactional
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	
	@Transactional
	void deleteAllById(Long id);
	
	List<ServiceTask> findAllByServiceCategoryID(Long serviceCategoryID);
	Optional<ServiceTask> findByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
	Optional<ServiceTask> findByTitleAndServiceCategoryID(String title,Long serviceCategoryID);
	
	@Transactional
	void deleteAllByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
}
