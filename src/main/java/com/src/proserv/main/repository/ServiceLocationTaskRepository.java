package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.ServiceLocationTask;

public interface ServiceLocationTaskRepository extends JpaRepository<ServiceLocationTask, Long> {
	Optional<ServiceLocationTask> findByLocationIDAndServiceTaskID(Long locationID, Long serviceTaskID);
	List<ServiceLocationTask> findAllByLocationID(Long locationID);
}
