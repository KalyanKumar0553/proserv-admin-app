package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.LocationServiceTask;
import com.src.proserv.main.model.LocationServiceTask;
import com.src.proserv.main.repository.LocationServiceTaskRepository;
import com.src.proserv.main.repository.LocationServiceTaskRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocationTaskService {

	private final LocationServiceTaskRepository repository;

	public LocationServiceTask save(LocationServiceTask task, String userUUID) {
		boolean exists = repository.findByLocationIDAndServiceTaskID(task.getLocationID(), task.getServiceTaskID())
				.isPresent();
		if (exists) {
			throw new RuntimeException("Service task already exists for this location.");
		}
		return repository.save(task);
	}

	public List<LocationServiceTask> findAll(Long locationID) {
		return repository.findAllByLocationID(locationID);
	}

	@Transactional
	public List<LocationServiceTask> saveAll(List<LocationServiceTask> tasks) {
		Set<String> uniqueKeys = new HashSet<>();
		List<LocationServiceTask> distinctTasks = tasks.stream().filter(task -> uniqueKeys.add(task.getLocationID() + "|" + task.getServiceTaskID())).toList();
		List<LocationServiceTask> existing = repository.findAll();
		Set<String> existingKeys = existing.stream().map(task -> task.getLocationID() + "|" + task.getServiceTaskID()).collect(Collectors.toSet());
		List<LocationServiceTask> filteredToSave = distinctTasks.stream().filter(task -> !existingKeys.contains(task.getLocationID() + "|" + task.getServiceTaskID())).toList();
		return repository.saveAll(filteredToSave);
	}

	public Optional<LocationServiceTask> findByLocationIdAndTaskId(Long locationID,Long taskID) {
		return repository.findByLocationIDAndServiceTaskID(locationID,taskID);
	}
	

	public LocationServiceTask update(Long locationID,Long id, LocationServiceTask updated, String userUUID) {
		return repository.findById(id).map(existing -> {
			existing.setEnabled(updated.getEnabled());
			return repository.save(existing);
		}).orElseThrow(() -> new AbstractRuntimeException(500,"Service task not found with details"));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void deleteLocationTask(Long locationID,Long taskID) {
		Optional<LocationServiceTask> taskHolder = repository.findByLocationIDAndServiceTaskID(locationID,taskID);
		if(taskHolder.isEmpty()) {
			throw new AbstractRuntimeException(500,"Service task not found with details");
		}
		repository.delete(taskHolder.get());
	}
}
