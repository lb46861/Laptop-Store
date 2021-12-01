package com.laptopstore.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Laptop {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String model;
	private String brand;
	private String operatingSystem;
	private String memory;
	private String storageSpace;
	private String category;
	private String graphicCard;
	private String proccessor;
	private String screen;
	private String warranty;

	private double shippingWeight;
	private double listPrice;
	private double ourPrice;
	private boolean active=true;
	
	@Column(columnDefinition="text")
	private String description;
	private int inStockNumber;
	
	@Transient
	private MultipartFile laptopImage;
	
	@OneToMany(mappedBy = "laptop")
	@JsonIgnore
	private List<LaptopToCartItem> laptopToCartItem;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getStorageSpace() {
		return storageSpace;
	}

	public void setStorageSpace(String storageSpace) {
		this.storageSpace = storageSpace;
	}

	public String getGraphicCard() {
		return graphicCard;
	}

	public void setGraphicCard(String graphicCard) {
		this.graphicCard = graphicCard;
	}

	public String getProccessor() {
		return proccessor;
	}

	public void setProccessor(String proccessor) {
		this.proccessor = proccessor;
	}
	
	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public double getShippingWeight() {
		return shippingWeight;
	}

	public void setShippingWeight(double shippingWeight) {
		this.shippingWeight = shippingWeight;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public double getOurPrice() {
		return ourPrice;
	}

	public void setOurPrice(double ourPrice) {
		this.ourPrice = ourPrice;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInStockNumber() {
		return inStockNumber;
	}

	public void setInStockNumber(int inStockNumber) {
		this.inStockNumber = inStockNumber;
	}

	public MultipartFile getLaptopImage() {
		return laptopImage;
	}

	public void setLaptopImage(MultipartFile laptopImage) {
		this.laptopImage = laptopImage;
	}

	public List<LaptopToCartItem> getLaptopToCartItem() {
		return laptopToCartItem;
	}

	public void setLaptopToCartItem(List<LaptopToCartItem> laptopToCartItem) {
		this.laptopToCartItem = laptopToCartItem;
	}
	
}
