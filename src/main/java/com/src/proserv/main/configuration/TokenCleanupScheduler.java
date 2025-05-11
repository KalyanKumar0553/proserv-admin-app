package com.src.proserv.main.configuration;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.src.proserv.main.repository.InvalidatedTokenRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TokenCleanupScheduler {

    private final InvalidatedTokenRepository tokenRepository;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void cleanUpExpiredTokens() {
    	System.out.println("Running Cleaning Token");
    	tokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
    }
}
