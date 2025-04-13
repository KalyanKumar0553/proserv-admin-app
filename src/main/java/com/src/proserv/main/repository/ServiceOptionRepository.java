package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceOption;

public interface ServiceOptionRepository extends JpaRepository<ServiceOption, Long> {

	List<ServiceOption> findAllByServiceOperationIDAndServiceCategoryID(Long serviceOperationID,Long serviceCategoryID);

	void deleteAllByServiceCategoryID(Long serviceCategoryID);

	void deleteAllByServiceOperationIDAndServiceCategoryID(Long serviceOperationID, Long serviceCategoryID);

	void deleteAllByIdAndServiceOperationIDAndServiceCategoryID(Long id,Long serviceOperationID, Long serviceCategoryID);

	void deleteById(Long id);

	Optional<ServiceOption> findByIdAndServiceOperationIDAndServiceCategoryID(Long id,Long serviceOperationID, Long serviceCategoryID);

	Optional<ServiceOption> findByNameAndServiceOperationIDAndServiceCategoryID(String name, Long serviceOperationID,Long serviceCategoryID);
}
