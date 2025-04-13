package com.src.proserv.main.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceCategory;


public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {
	Optional<ServiceCategory> findByName(String name);
	Optional<ServiceCategory> findByIdAndName(Long id,String name);
	ServiceCategory save(ServiceCategory category);
}
