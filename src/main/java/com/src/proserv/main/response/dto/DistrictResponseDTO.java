package com.src.proserv.main.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictResponseDTO {
	Long id;
	String stateCode;
	String name;
}
