package com.src.proserv.main.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.Provider;


public interface ProviderRepository extends JpaRepository<Provider, Long> {

}
