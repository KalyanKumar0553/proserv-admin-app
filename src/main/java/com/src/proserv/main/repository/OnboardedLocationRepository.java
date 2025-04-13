package com.src.proserv.main.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.OnboardedLocation;


public interface OnboardedLocationRepository extends JpaRepository<OnboardedLocation, Long> {
	Optional<OnboardedLocation> findByStateIDAndDistrictIDAndName(Long stateID, Long districtID, String name);
}
