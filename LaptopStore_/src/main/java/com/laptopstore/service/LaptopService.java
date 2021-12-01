package com.laptopstore.service;

import java.util.List;

import com.laptopstore.domain.Laptop;

public interface LaptopService {
	List<Laptop> findAll();
	
	Laptop findById(Long id);
	
	List<Laptop> findByCategory(String category);
	
	List<Laptop> blurrySearch(String model);
}
