package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.domain.Laptop;
import com.adminportal.repository.LaptopRepository;
import com.adminportal.service.LaptopService;

@Service
public class LaptopServiceImpl implements LaptopService{

	@Autowired
	private LaptopRepository laptopRepository;
	
	public Laptop save(Laptop laptop) {
		return laptopRepository.save(laptop);
	}
	
	public List<Laptop> findAll(){
		return (List<Laptop>)laptopRepository.findAll();
	}
	
	public Laptop findById(Long id) {
		return laptopRepository.findById(id).get();
	}
	
	public void removeOne(Long id) {
		laptopRepository.deleteById(id);
	}
}
