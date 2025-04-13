package com.src.proserv.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.src.proserv.main.model.UserAddress;


public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
	List<UserAddress> findAllByUserUUID(String userID);
	Optional<UserAddress> findByAddressIDAndUserUUID(Long addressID,String userID);
}
