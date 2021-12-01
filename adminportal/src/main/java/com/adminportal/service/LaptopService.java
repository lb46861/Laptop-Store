package com.adminportal.service;

import java.util.List;

import com.adminportal.domain.Laptop;

public interface LaptopService {
	Laptop save(Laptop laptop);
	
	List<Laptop> findAll();
	
	Laptop findById(Long id);
	
	void removeOne(Long id);
}
