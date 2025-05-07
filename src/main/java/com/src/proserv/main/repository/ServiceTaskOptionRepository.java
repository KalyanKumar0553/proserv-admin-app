package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.src.proserv.main.model.ServiceTaskOption;

public interface ServiceTaskOptionRepository extends JpaRepository<ServiceTaskOption, Long> {
	@Transactional
	void deleteAllByServiceCategoryID(Long serviceCategoryID);
	
	@Transactional
	void deleteAllByIdAndServiceCategoryID(Long id,Long serviceCategoryID);
	
	@Transactional
	void deleteAllByIdAndServiceCategoryIDAndServiceTaskID(Long id,Long serviceCategoryID,Long serviceTaskID);
	
	@Transactional
	void deleteAllByServiceCategoryIDAndServiceTaskID(Long serviceCategoryID,Long serviceTaskID);
	@Transactional
	void deleteById(Long id);

	Optional<ServiceTaskOption> findByIdAndServiceCategoryIDAndServiceTaskID(Long id,Long serviceCategoryID,Long serviceTaskID);

	Optional<ServiceTaskOption> findByNameAndServiceCategoryID(String name,Long serviceCategoryID);
	
	List<ServiceTaskOption> findAllByServiceCategoryIDAndServiceTaskID(Long serviceCategoryID,Long serviceTaskID);
}
