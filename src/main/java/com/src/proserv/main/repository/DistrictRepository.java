package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.District;

public interface DistrictRepository extends JpaRepository<District, Long> {
	List<District> findAllByStateCode(String stateCode);
	void deleteAllByStateCode(String stateCode);
	Optional<District> findByIdAndStateCode(Long ID,String stateCode);
	Optional<District> findByStateCodeAndName(String stateCode,String name);
}
