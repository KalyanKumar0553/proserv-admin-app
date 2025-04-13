package com.src.proserv.main.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JSONResponseDTO<T> {
	T statusMsg;
	boolean isError;
}
