package com.src.proserv.main.services;

import org.springframework.stereotype.Service;

import com.src.proserv.main.repository.ServiceCategoryRepository;
import com.src.proserv.main.response.dto.OverviewResponseDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OverviewService {

	final ServiceCategoryRepository categoryRepository;

	public OverviewResponseDTO getOverviewData() {
		return OverviewResponseDTO.builder().categoriesCount(categoryRepository.getTotalCategories()).build();
	}
}
