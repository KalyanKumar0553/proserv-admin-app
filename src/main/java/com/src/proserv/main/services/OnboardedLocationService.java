package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.OnboardedLocation;
import com.src.proserv.main.repository.OnboardedLocationRepository;

@Service
public class OnboardedLocationService {

    @Autowired
    private OnboardedLocationRepository repository;


    public String save(OnboardedLocation location,String userUUID) {
    	boolean exists = repository.findByStateIDAndDistrictIDAndName(location.getStateID(), location.getDistrictID(), location.getName()).isPresent();
    	if(exists) {
    		throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Location already Onboarded");
    	}
    	location.setCreatedBy(userUUID);
    	location.setCreatedOn(LocalDateTime.now());
    	location.setLastModifiedBy(userUUID);
    	location.setLastModifiedOn(LocalDateTime.now());
    	repository.save(location);
        return "Location Onboarded Successfully";
    }

    public List<OnboardedLocation> getAllLocations() {
        return repository.findAll();
    }

    public Optional<OnboardedLocation> findById(Long id) {
    	boolean exists = repository.findById(id).isPresent();
    	if(!exists) {
    		throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Location not found with ID");
    	}
        return repository.findById(id);
    }

    public OnboardedLocation update(Long id, OnboardedLocation updated,String userUUID) {
        return repository.findById(id).map(loc -> {
            loc.setStateID(updated.getStateID());
            loc.setDistrictID(updated.getDistrictID());
            loc.setName(updated.getName());
            loc.setEnabled(updated.getEnabled());
            loc.setLastModifiedBy(userUUID);
            loc.setLastModifiedOn(LocalDateTime.now());
            return repository.save(loc);
        }).orElseThrow(() -> new RuntimeException("Location not found with ID"));
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }
}
