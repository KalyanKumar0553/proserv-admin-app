package com.src.proserv.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.State;

public interface StateRepository extends JpaRepository<State, Long> {
	Optional<State> findByCode(String code);
	Optional<State> findByName(String name);
}
