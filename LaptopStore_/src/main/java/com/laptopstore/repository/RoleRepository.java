package com.laptopstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.laptopstore.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{
	Role findByname(String name);
}
