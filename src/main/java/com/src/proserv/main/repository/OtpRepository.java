package com.src.proserv.main.repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.src.proserv.main.model.Otp;


public interface OtpRepository extends JpaRepository<Otp, Long> {

	Optional<Otp> findFirstByUsernameOrderByCreatedAtDesc(String username);

    Optional<Otp> findByUsernameAndCreatedAtBetweenOrderByCreatedAtAsc(String username,LocalDateTime startTime,LocalDateTime endTime);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM OtpAttempt o WHERE o.username = :username")
	void deleteAllByUsername(String username);
}
