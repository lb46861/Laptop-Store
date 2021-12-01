package com.adminportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.adminportal.domain.Laptop;
import com.adminportal.service.LaptopService;

@RestController
public class ResourceController {

	@Autowired
	private LaptopService laptopService;
	
	@RequestMapping(value="/laptop/removeList", method=RequestMethod.POST)
	public String removeList(
			@RequestBody ArrayList<String> laptopIdList, Model model
			){
		
		for (String id : laptopIdList) {
			laptopService.removeOne(Long.parseLong(id.substring(10)));
		}
		
		return "delete success";
	}
	
	@RequestMapping(value="/laptop/remove", method=RequestMethod.POST)
	public String remove(
			@ModelAttribute("id") String id, Model model
			) {
		laptopService.removeOne(Long.parseLong(id.substring(10)));
		List<Laptop> laptopList = laptopService.findAll();
		model.addAttribute("laptopList", laptopList);
		
		return "redirect:/laptop/laptopList";
	}
}