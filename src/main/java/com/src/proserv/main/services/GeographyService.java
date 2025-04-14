package com.src.proserv.main.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.District;
import com.src.proserv.main.model.State;
import com.src.proserv.main.repository.DistrictRepository;
import com.src.proserv.main.repository.StateRepository;
import com.src.proserv.main.request.dto.DistrictRequestDTO;
import com.src.proserv.main.request.dto.StateRequestDTO;
import com.src.proserv.main.response.dto.DistrictResponseDTO;
import com.src.proserv.main.response.dto.StateResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GeographyService {

	final StateRepository stateRepository;

	final DistrictRepository districtRepository;

	public List<StateResponseDTO> getStates() {
		return stateRepository.findAll().stream().map(s -> new StateResponseDTO(s.getId(),s.getCode(), s.getName()))
				.collect(Collectors.toList());
	}

	public String updateState(String userUUID,StateRequestDTO stateRequest) {
		Optional<State> stateHolder = stateRepository.findByCode(stateRequest.getCode());
		if(stateHolder.isPresent()) {
			State currState = stateHolder.get();
			currState.setName(stateRequest.getName());
			stateRepository.save(currState);
		} else {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable To Update State");
		}
		return "Succesfully Updated State Details";
	}

	public String createState(String userUUID,StateRequestDTO stateRequest) {
		State currState = State.builder().name(stateRequest.getName()).code(stateRequest.getCode()).build();
		currState.setName(stateRequest.getName());
		stateRepository.save(currState);
		return "Succesfully Added State";
	}

	public String deleteState(String code) {
		Optional<State> stateHolder = stateRepository.findByCode(code);
		if(stateHolder.isPresent()) {
			State currState = stateHolder.get();
			String stateCode = currState.getCode();
			stateRepository.delete(currState);
			districtRepository.deleteAllByStateCode(stateCode);
		} else {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable To Delete State");
		}
		return "Succesfully Deleted State Details";
	}

	public List<DistrictResponseDTO> getDistricts(String stateCode) {
		return districtRepository.findAllByStateCode(stateCode).stream().map(s -> new DistrictResponseDTO(s.getId(),s.getStateCode(),s.getName()))
				.collect(Collectors.toList());
	}

	public String createDistrict(String userUUID,DistrictRequestDTO districtRequest) {
		District currDistrict = District.builder().stateCode(districtRequest.getStateCode()).name(districtRequest.getDistrictName()).build();
		currDistrict = DistrictRequestDTO.toEntityFromDistrictRequestDTO(currDistrict, districtRequest, userUUID);
		Optional<District> existingDistrictHolder = districtRepository.findByStateCodeAndName(districtRequest.getStateCode(),districtRequest.getDistrictName());
		if(existingDistrictHolder.isPresent()) {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "District with State code and Name already exists");
		}
		districtRepository.save(currDistrict);
		return "Succesfully Created District Details";
	}

	public String updateDistrict(String userUUID,DistrictRequestDTO districtRequest) {
		Optional<District> districtHolder = districtRepository.findByIdAndStateCode(districtRequest.getDistrictID(),districtRequest.getStateCode());
		if(districtHolder.isPresent()) {
			District currDistrict = districtHolder.get();
			currDistrict = DistrictRequestDTO.toEntityFromDistrictRequestDTO(currDistrict, districtRequest, userUUID);
			districtRepository.save(currDistrict);
		} else {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable To Find District with District ID and State Code");
		}
		return "Succesfully Updated State Details";
	}

	public String deleteDistrict(String stateCode,Long districtID) {
		Optional<District> districtHolder = districtRepository.findById(districtID);
		if(districtHolder.isPresent()) {
			District currDistrict = districtHolder.get();
			districtRepository.delete(currDistrict);
		} else {
			throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable To Find District with ID");
		}
		return "Succesfully Deleted District Details";
	}
}
