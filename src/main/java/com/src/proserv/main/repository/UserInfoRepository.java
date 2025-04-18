package com.src.proserv.main.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.src.proserv.main.model.UserInfo;


public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
	Optional<UserInfo> findByEmail(String email);
	Optional<UserInfo> findByMobile(String mobile);
    Optional<UserInfo> findByUsername(String username);
    Optional<UserInfo> findByEmailOrMobile(String email,String mobile);
    @Query("SELECT u.UUID FROM UserInfo u WHERE u.UUID IN :userIds")
    List<String> findExistingUserIds(List<String> userIds);
    
    @Modifying
    @Query("DELETE FROM UserInfo u WHERE u.enabled=false and u.createdOn <= :now")
    void deleteAllNotEnabledUsers(LocalDateTime now);
}
