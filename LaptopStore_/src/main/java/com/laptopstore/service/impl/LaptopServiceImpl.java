package com.laptopstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptopstore.domain.Laptop;
import com.laptopstore.repository.LaptopRepository;
import com.laptopstore.service.LaptopService;

@Service
public class LaptopServiceImpl implements LaptopService{
	
	@Autowired
	LaptopRepository laptopRepository;
	
	public List<Laptop> findAll(){
		List<Laptop> laptopList = (List<Laptop>) laptopRepository.findAll();
		List<Laptop> activeLaptopList = new ArrayList<>();
		for(Laptop laptop : laptopList) {
			if(laptop.isActive()) {
				activeLaptopList.add(laptop);
			}
		}
		return activeLaptopList;
		
	}

	public Laptop findById(Long id) {
		return laptopRepository.findById(id).get();
	}
	
	public 	List<Laptop> findByCategory(String category){
		List<Laptop> laptopList = laptopRepository.findByCategory(category);
		List<Laptop> activeLaptopList = new ArrayList<>();
		
		for(Laptop laptop : laptopList) {
			if(laptop.isActive()) {
				activeLaptopList.add(laptop);
			}
		}
		
		return activeLaptopList;
	}
	
	
	public List<Laptop> blurrySearch(String model){
		List<Laptop> laptopList = laptopRepository.findByModelContaining(model);
		
		List<Laptop> activeLaptopList = new ArrayList<>();
		
		for(Laptop laptop : laptopList) {
			if(laptop.isActive()) {
				activeLaptopList.add(laptop);
			}
		}
		
		return activeLaptopList;
	}
}
