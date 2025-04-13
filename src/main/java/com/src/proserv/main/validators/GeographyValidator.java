package com.src.proserv.main.validators;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.District;
import com.src.proserv.main.model.State;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.repository.DistrictRepository;
import com.src.proserv.main.repository.StateRepository;
import com.src.proserv.main.repository.UserInfoRepository;
import com.src.proserv.main.request.dto.DistrictRequestDTO;
import com.src.proserv.main.request.dto.StateRequestDTO;
import com.src.proserv.main.request.dto.UserAddressRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GeographyValidator {

    private final UserInfoRepository userInfoRepository;

    private final DistrictRepository districtRepository;

    private final StateRepository stateRepo;

	public void validateStateRequestForUpdate(StateRequestDTO stateRequest) {
		Optional<String> stateCode = Optional.ofNullable(stateRequest.getCode());
		Optional<String> stateName = Optional.ofNullable(stateRequest.getName());
		if (stateCode.isEmpty() || stateCode.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "State Code is Required to Update");
		}
		if (stateName.isEmpty() || stateName.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Name Should not be Empty");
		}
	}

	public void validateStateRequestForCreate(StateRequestDTO stateRequest) {
		validateStateRequestForCreate(stateRequest);
	}

	public void validateDistrictRequestForDelete(String stateCode,Long districtID) {
		Optional<String> stateCodeHolder =  Optional.ofNullable(stateCode);
		Optional<Long> districtIDHolder =  Optional.ofNullable(districtID);
		if (stateCodeHolder.isEmpty() || stateCodeHolder.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "State Code is Required to Update District Details");
		}
		if (districtID==null || districtID <=0) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "District Unique ID is Required to Update District Details");
		}
	}

	public void validateDistrictRequestForCreate(DistrictRequestDTO districtRequest) {
		Optional<String> stateCode = Optional.ofNullable(districtRequest.getStateCode());
		Optional<String> districtName = Optional.ofNullable(districtRequest.getDistrictName());
		if (stateCode.isEmpty() || stateCode.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "State Code is Required to Update District Details");
		}
		if (districtName.isEmpty() || districtName.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Name Should not be Empty for District");
		}
	}

	public void validateDistrictRequestForUpdate(DistrictRequestDTO districtRequest) {
		validateDistrictRequestForCreate(districtRequest);
	}
}
