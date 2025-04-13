package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceTask;


public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long> {
	void deleteAllByServiceOperationIDAndServiceCategoryID(Long serviceOperationID,Long serviceCategoryID);
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	void deleteAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long optionID,Long operationID,Long categoryID);
	void deleteAllByIdAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long id,Long optionID,Long operationID,Long categoryID);
	List<ServiceTask> findAllByServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long serviceOptionID,Long serviceOperationID,Long serviceCategoryID);
	Optional<ServiceTask> findByIdAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(Long id,Long serviceOptionID,Long serviceOperationID,Long serviceCategoryID);
	Optional<ServiceTask> findByNameAndServiceOptionIDAndServiceOperationIDAndServiceCategoryID(String name,Long serviceOptionID, Long serviceOperationID, Long serviceCategoryID);
}
