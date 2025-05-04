package com.src.proserv.main.request.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ServiceCategoryRequestDTO {
	private Long id;
	
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters and spaces")
    @NotNull(message = "Category Name is required")
	@NotEmpty(message = "Category Name is required")
	private String name;
	
	@NotNull(message = "Category Display Image is required")
	@NotEmpty(message = "Category Display Image is required")
	private String displayURL;
	
	private boolean enabled;
}
