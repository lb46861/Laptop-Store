package com.laptopstore.repository;



import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.laptopstore.domain.Laptop;

public interface LaptopRepository extends CrudRepository<Laptop, Long>{

	List<Laptop> findByCategory(String category);
	
	List<Laptop> findByModelContaining(String model);
}
