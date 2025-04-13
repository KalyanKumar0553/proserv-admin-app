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
import com.src.proserv.main.request.dto.UserAddressRequestDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserAddressValidator {

    private final UserInfoRepository userInfoRepository;

    private final DistrictRepository districtRepository;

    private final StateRepository stateRepo;

	public void validateUserAddress(UserAddressRequestDTO addressRequest, String username) {
		Optional<String> stateCode = Optional.ofNullable(addressRequest.getStateCode());
		Optional<String> district = Optional.ofNullable(addressRequest.getDistrictCode());
		Optional<String> location = Optional.ofNullable(addressRequest.getLocation());
		Optional<String> city = Optional.ofNullable(addressRequest.getCity());
		Optional<String> street = Optional.ofNullable(addressRequest.getStreet());
		Optional<String> houseNumber = Optional.ofNullable(addressRequest.getHouseNumber());
		if (stateCode.isEmpty() || stateCode.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "State is Required to Add address");
		}
		if (district.isEmpty() || district.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "District is Required to Add address");
		}
		if (city.isEmpty() || city.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "City is Required to Add address");
		}
		if (location.isEmpty() || location.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Location Details are Required to Add address");
		}
		if (street.isEmpty() || street.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Street Details are Required to Add address");
		}
		if (houseNumber.isEmpty() || houseNumber.get().isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "House Number Details are Required to Add address");
		}
		Optional<State> stateHolder = stateRepo.findByCode(stateCode.get());
		if(stateHolder.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid State Details");
		}
		Optional<District> districtHolder = districtRepository.findByStateCodeAndName(stateCode.get(),district.get());
		if(districtHolder.isEmpty()) {
			throw new AbstractRuntimeException(HttpStatus.BAD_REQUEST.value(), "Invalid District Details");
		}
	}
}
