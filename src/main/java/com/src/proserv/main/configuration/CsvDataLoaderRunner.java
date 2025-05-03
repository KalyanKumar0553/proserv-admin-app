package com.src.proserv.main.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.src.proserv.main.services.CsvDataLoaderService;

@Component
public class CsvDataLoaderRunner implements ApplicationRunner {

	@Value("${load.default-data}")
	private boolean loadDefaultData;
	
    private final CsvDataLoaderService csvDataLoaderService;

    public CsvDataLoaderRunner(CsvDataLoaderService csvDataLoaderService) {
        this.csvDataLoaderService = csvDataLoaderService;
    }

    @Override
    public void run(ApplicationArguments args) {
    	if(loadDefaultData) {
    		new Thread(csvDataLoaderService::loadAllCsvFiles).start();	
    	}
    }
}
