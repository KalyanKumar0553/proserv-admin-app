package com.src.proserv.main.configuration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.src.proserv.main.model.InvalidatedToken;
import com.src.proserv.main.repository.InvalidatedTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JWTTokenProvider {
    
	private final String JWT_SECRET = "6rvQrbY7/yDbU6JfDdpHA9gN5Q/w7fhgJEBde0x6CTJtV8Pyyyhqaw+k5HKbfMlvg6nstoAZ2SjkZfte7Ehgqg==";
    
    private final long JWT_EXPIRATION = 18 * 60 * 1000L;
    
    private final InvalidatedTokenRepository tokenRepository;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        LocalDateTime exp = Instant.ofEpochMilli(expiration.getTime())
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();
        return exp.isBefore(LocalDateTime.now());
    }
    
    @Transactional
    public void invalidateToken(String token) {
        Date expirationDate = extractExpiration(token);
        LocalDateTime expiration = Instant.ofEpochMilli(expirationDate.getTime())
                                          .atZone(ZoneId.systemDefault())
                                          .toLocalDateTime();
        InvalidatedToken invalidateToken = InvalidatedToken.builder().token(token).expirationDate(expiration).build();
        tokenRepository.save(invalidateToken);
    }

    public boolean isTokenInvalidated(String token) {
        return tokenRepository.existsByToken(token);
    }
    
    public String generateToken(Authentication authentication, String string) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        roles.add("ROLE_USER");
        roles.add("USER");
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userUUID", string)
                .claim("roles", roles)
                .claim(Claims.EXPIRATION, expiryDate)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public String getUserIDFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(JWT_SECRET)
            .parseClaimsJws(token)
            .getBody();
        return claims.get("userUUID", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
        	if (isTokenExpired(authToken)) {
                return false;
            }
        	if (isTokenInvalidated(authToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    
  
}
