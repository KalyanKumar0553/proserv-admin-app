package com.src.proserv.main.response.dto;

import java.time.LocalDateTime;
import com.src.proserv.main.model.UserAddress;
import com.src.proserv.main.model.UserInfo;
import com.src.proserv.main.request.dto.UserAddressRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressResponseDTO {
	private Long addressID;
	private String stateCode;
	private String districtCode;
	private String city;
	private String labelName;
	private String location;
	private String street;
	private String houseNumber;
	private String landmark;
	private String contactPerson;
	private String contactMobile;

	public static UserAddressResponseDTO fromEntityToUserAddressResponsetDTO(UserAddress currAddress) {
		return UserAddressResponseDTO.builder().addressID(currAddress.getAddressID())
				.stateCode(currAddress.getStateCode()).districtCode(currAddress.getDistrictCode())
				.city(currAddress.getCity()).labelName(currAddress.getLabelName()).location(currAddress.getLocation())
				.street(currAddress.getStreet()).houseNumber(currAddress.getHouseNumber())
				.landmark(currAddress.getLandmark()).contactPerson(currAddress.getContactPerson())
				.contactMobile(currAddress.getContactMobile()).build();
	}
}
