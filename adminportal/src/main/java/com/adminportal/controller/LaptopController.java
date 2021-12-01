package com.adminportal.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.domain.Laptop;
import com.adminportal.service.LaptopService;

@Controller
@RequestMapping("/laptop")
public class LaptopController {
	
	@Autowired
	private LaptopService laptopService;

	@RequestMapping(value="/add", method=RequestMethod.GET)
	public String addLaptop(Model model) {
		Laptop laptop = new Laptop();
		model.addAttribute("laptop", laptop);
		return "addLaptop";
	}
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public String addLaptopPost(
			@ModelAttribute("laptop")Laptop laptop, HttpServletRequest request
			) {
		laptopService.save(laptop);
		
		MultipartFile laptopImage = laptop.getLaptopImage();
		
		try {
			byte[] btytes = laptopImage.getBytes();
			String name = laptop.getId()+".png";
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/images/laptop/"+name)));
			stream.write(btytes);
			stream.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:laptopList";
	}
	
	@RequestMapping("/laptopInfo")
	public String laptopInfo(
			@RequestParam("id") Long id, Model model
			) {
		Laptop laptop = laptopService.findById(id);
		model.addAttribute("laptop", laptop);
		
		return "laptopInfo";
	}
	
	@RequestMapping("/updateLaptop")
	public String updateLaptop(@RequestParam("id") Long id, Model model) {
		Laptop laptop = laptopService.findById(id);
		model.addAttribute("laptop", laptop);
		
		return "updateLaptop";
	}
	
	@RequestMapping(value="/updateLaptop", method = RequestMethod.POST)
	public String updateLaptopPost(@ModelAttribute("laptop") Laptop laptop, HttpServletRequest request) {
		laptopService.save(laptop);
		
		MultipartFile laptopImage = laptop.getLaptopImage();
		
		if(!laptopImage.isEmpty()) {
			try {
				byte[] btytes = laptopImage.getBytes();
				String name = laptop.getId()+".png";
				
				Files.delete(Paths.get("src/main/resources/static/images/laptop/" + name));
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/images/laptop/"+name)));
				stream.write(btytes);
				stream.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/laptop/laptopInfo?id="+laptop.getId();
	}
	
	
	@RequestMapping("/laptopList")
	public String laptopList(Model model) {
		List<Laptop> laptopList = laptopService.findAll();
		model.addAttribute("laptopList", laptopList);
		return "laptopList";
	}
	
	
	
}
