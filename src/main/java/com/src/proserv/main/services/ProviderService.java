package com.src.proserv.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.src.proserv.main.exceptions.AbstractRuntimeException;
import com.src.proserv.main.model.Provider;
import com.src.proserv.main.repository.ProviderRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProviderService {

	final ProviderRepository providerRepository;

    public Provider saveProvider(Provider provider,String userUUID) {
    	return providerRepository.save(provider);
    }

    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    public Provider getProviderById(Long id) {
    	Optional<Provider> existingProvider = providerRepository.findById(id);
    	if(existingProvider.isEmpty()) {
    		throw new AbstractRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Unable to find provider details");
    	}
    	return existingProvider.get();
    }

    public String updateProvider(Long id, Provider updatedProvider,String userUUID) {
        providerRepository.findById(id).map(provider -> {
            provider.setName(updatedProvider.getName());
            provider.setMobile(updatedProvider.getMobile());
            provider.setVerified(updatedProvider.getVerified());
            provider.setPreferredTimes(updatedProvider.getPreferredTimes());
            provider.setPreferredDays(updatedProvider.getPreferredDays());
            provider.setGender(updatedProvider.getGender());
            return providerRepository.save(provider);
        }).orElseThrow(() -> new RuntimeException("Provider not found with ID: " + id));
        return "Succesfully Updated Provider";
    }

    public void deleteProvider(Long id) {
        providerRepository.deleteById(id);
    }
}
