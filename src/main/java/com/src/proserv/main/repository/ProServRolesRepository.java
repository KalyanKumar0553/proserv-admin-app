package com.src.proserv.main.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.src.proserv.main.model.ProServRoles;

public interface ProServRolesRepository extends JpaRepository<ProServRoles, Long> {
	Optional<ProServRoles> findByUserUUID(String userID);
}
