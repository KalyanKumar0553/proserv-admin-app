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
import com.src.proserv.main.model.ServiceLocationTask;
import com.src.proserv.main.repository.ServiceLocationTaskRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ServiceLocationTaskService {

	private final ServiceLocationTaskRepository repository;

	public ServiceLocationTask save(ServiceLocationTask task, String userUUID) {
		boolean exists = repository.findByLocationIDAndServiceTaskID(task.getLocationID(), task.getServiceTaskID())
				.isPresent();
		if (exists) {
			throw new RuntimeException("Service task already exists for this location.");
		}
		return repository.save(task);
	}

	public List<ServiceLocationTask> findAll(Long locationID) {
		return repository.findAllByLocationID(locationID);
	}

	@Transactional
	public List<ServiceLocationTask> saveAll(List<ServiceLocationTask> tasks) {
		Set<String> uniqueKeys = new HashSet<>();
		List<ServiceLocationTask> distinctTasks = tasks.stream().filter(task -> uniqueKeys.add(task.getLocationID() + "|" + task.getServiceTaskID())).toList();
		List<ServiceLocationTask> existing = repository.findAll();
		Set<String> existingKeys = existing.stream().map(task -> task.getLocationID() + "|" + task.getServiceTaskID()).collect(Collectors.toSet());
		List<ServiceLocationTask> filteredToSave = distinctTasks.stream().filter(task -> !existingKeys.contains(task.getLocationID() + "|" + task.getServiceTaskID())).toList();
		return repository.saveAll(filteredToSave);
	}

	public Optional<ServiceLocationTask> findByLocationIdAndId(Long locationID,Long taskID) {
		return repository.findByLocationIDAndServiceTaskID(locationID,taskID);
	}

	public ServiceLocationTask update(Long locationID,Long id, ServiceLocationTask updated, String userUUID) {
		return repository.findById(id).map(existing -> {
			existing.setEnabled(updated.getEnabled());
			return repository.save(existing);
		}).orElseThrow(() -> new AbstractRuntimeException(500,"Service task not found with details"));
	}

	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void deleteLocationTask(Long locationID,Long taskID) {
		Optional<ServiceLocationTask> taskHolder = repository.findByLocationIDAndServiceTaskID(locationID,taskID);
		if(taskHolder.isEmpty()) {
			throw new AbstractRuntimeException(500,"Service task not found with details");
		}
		repository.delete(taskHolder.get());
	}
}
