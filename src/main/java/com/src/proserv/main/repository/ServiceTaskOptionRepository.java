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
	void deleteAllByServiceCategoryIDAndServiceTaskID(Long serviceCategoryID,Long serviceTaskID);
	@Transactional
	void deleteById(Long id);

	Optional<ServiceTaskOption> findByIdAndServiceCategoryID(Long id,Long serviceCategoryID);

	Optional<ServiceTaskOption> findByNameAndServiceCategoryID(String name,Long serviceCategoryID);
	
	List<ServiceTaskOption> findAllByServiceCategoryID(Long serviceCategoryID);
}
