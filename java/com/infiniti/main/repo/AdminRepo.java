package com.infiniti.main.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infiniti.main.modal.Admin;



public interface AdminRepo extends JpaRepository<Admin, Integer>{
	@Query("select a from Admin a where a.name=:n")
	public Admin getUserByUserName(@Param("n") String name);
}
