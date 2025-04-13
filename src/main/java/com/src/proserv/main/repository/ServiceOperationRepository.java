package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceOperation;


public interface ServiceOperationRepository extends JpaRepository<ServiceOperation, Long> {
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	void deleteAllByIdAndServiceCategoryID(Long serviceOperationID,Long serviceCategoryID);
	List<ServiceOperation> findAllByServiceCategoryID(Long serviceCategoryID);
	Optional<ServiceOperation> findByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
	Optional<ServiceOperation> findByServiceCategoryIDAndNameAndServiceTitle(Long serviceCategoryID,String name,String title);
}
