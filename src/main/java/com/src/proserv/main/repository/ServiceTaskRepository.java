package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceTask;


public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long> {
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	void deleteAllById(Long id);
	List<ServiceTask> findAllByServiceCategoryID(Long serviceCategoryID);
	Optional<ServiceTask> findByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
	Optional<ServiceTask> findByTitleAndServiceCategoryID(String title,Long serviceCategoryID);
	void deleteAllByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
}
