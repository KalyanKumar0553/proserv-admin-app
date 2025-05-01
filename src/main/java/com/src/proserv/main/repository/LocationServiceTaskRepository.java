package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.LocationServiceTask;

public interface LocationServiceTaskRepository extends JpaRepository<LocationServiceTask, Long> {
	Optional<LocationServiceTask> findByLocationIDAndServiceTaskID(Long locationID, Long serviceTaskID);
	List<LocationServiceTask> findAllByLocationID(Long locationID);
	void deleteByLocationIDAndServiceTaskID(Long locationID, Long serviceTaskID);
}
