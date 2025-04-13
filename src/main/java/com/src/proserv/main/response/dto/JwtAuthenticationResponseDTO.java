package com.src.proserv.main.response.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationResponseDTO {
	private String accessToken;
    private String tokenType = "Bearer";
    public JwtAuthenticationResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
