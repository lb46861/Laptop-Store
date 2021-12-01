package com.laptopstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.User;
import com.laptopstore.service.LaptopService;
import com.laptopstore.service.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LaptopService laptopService;
	
	@RequestMapping("/searchByCategory")
	public String searchByCategory(
			@RequestParam("category") String category,
			Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		String classActiveCategory = "active"+category;
		model.addAttribute(classActiveCategory, true);
		
		List<Laptop> laptopList = laptopService.findByCategory(category);
		
		if(laptopList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "laptopview";
		}
		
		model.addAttribute("laptopList", laptopList);
		return "laptopview";
	}
	
	@RequestMapping("/searchLaptop")
	public String searchLaptop(
			@ModelAttribute("keyword") String keyword,
			Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		List<Laptop> laptopList = laptopService.blurrySearch(keyword);
		
		if(laptopList.isEmpty()) {
			model.addAttribute("emptyList", true);
			return "laptopview";
		}
		
		model.addAttribute("laptopList", laptopList);
		return "laptopview";
	}
}
