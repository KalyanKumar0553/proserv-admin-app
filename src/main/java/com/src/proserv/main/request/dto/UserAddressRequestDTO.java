package com.src.proserv.main.request.dto;

import com.src.proserv.main.model.Auditable;
import com.src.proserv.main.model.UserAddress;
import com.src.proserv.main.model.UserInfo;

import lombok.Data;

@Data
public class UserAddressRequestDTO extends Auditable {
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

	public static UserAddress toEntityFromUserAddressRequestDTO(UserAddress currAddress,UserAddressRequestDTO addressRequest,UserInfo user) {
		currAddress.setUserUUID(user.getUUID());
		currAddress.setStateCode(addressRequest.getStateCode());
		currAddress.setDistrictCode(addressRequest.getDistrictCode());
		currAddress.setCity(addressRequest.getCity());
		currAddress.setLabelName(addressRequest.getLabelName());
		currAddress.setLocation(addressRequest.getLocation());
		currAddress.setStreet(addressRequest.getStreet());
		currAddress.setHouseNumber(addressRequest.getHouseNumber());
		currAddress.setLandmark(addressRequest.getLandmark());
		currAddress.setContactPerson(addressRequest.getContactPerson());
		currAddress.setContactMobile(addressRequest.getContactMobile());
		return currAddress;
    }
}
