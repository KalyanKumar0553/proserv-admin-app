package com.src.proserv.main.request.dto;

import java.time.LocalDateTime;

import com.src.proserv.main.model.District;

import lombok.Data;

@Data
public class DistrictRequestDTO {
	private Long districtID;
	private String stateCode;
	private String districtName;

	public static District toEntityFromDistrictRequestDTO(District district,DistrictRequestDTO request,String userUUID) {
		district.setName(request.getDistrictName());
		return district;
	}
}
