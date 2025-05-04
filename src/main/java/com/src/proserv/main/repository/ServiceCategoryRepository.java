package com.src.proserv.main.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.src.proserv.main.model.ServiceCategory;


public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
	@Query("SELECT COUNT(st) FROM ServiceCategory st where st.enabled = true")
	long getTotalCategories();
	Optional<ServiceCategory> findByName(String name);
	Optional<ServiceCategory> findByIdAndName(Long id,String name);
	ServiceCategory save(ServiceCategory category);
}
