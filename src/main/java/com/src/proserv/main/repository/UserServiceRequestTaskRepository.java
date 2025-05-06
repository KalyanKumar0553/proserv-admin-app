package com.src.proserv.main.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.src.proserv.main.model.UserServiceRequestTask;


public interface UserServiceRequestTaskRepository extends JpaRepository<UserServiceRequestTask, Long> {


	@Query("SELECT COUNT(st.id) FROM UserServiceRequestTask st WHERE st.serviceCategoryID = :serviceCategoryID and COALESCE(st.status,'UNASSIGNED')<>'COMPLETED'")
	long countIncompleteTasksByServiceCategoryID(@Param("serviceCategoryID") Long serviceCategoryID);


	@Query("SELECT COUNT(st.id) FROM UserServiceRequestTask st WHERE st.serviceOptionID = :serviceOptionID and COALESCE(status,'UNASSIGNED')<>'COMPLETED'")
	long countIncompleteTasksByServiceOptionID(@Param("serviceOptionID") Long serviceOptionID);


	@Query("SELECT COUNT(st.id) FROM UserServiceRequestTask st WHERE st.id = :serviceTaskID and COALESCE(status,'UNASSIGNED')<>'COMPLETED'")
	long countIncompleteTasksByServiceTaskID(@Param("serviceTaskID") Long serviceTaskID);
}
