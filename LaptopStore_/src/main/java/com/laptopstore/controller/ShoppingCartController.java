package com.laptopstore.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;
import com.laptopstore.service.CartItemService;
import com.laptopstore.service.LaptopService;
import com.laptopstore.service.ShoppingCartService;
import com.laptopstore.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private LaptopService laptopService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		model.addAttribute("user", user);
		
		return "shoppingCart";
	}
	
	
	@RequestMapping("/addItem")
	public String addItem(
			@ModelAttribute("laptop") Laptop laptop,
			@ModelAttribute("qty") String qty,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		Long id = laptop.getId();
		laptop = laptopService.findById(id);
		
		if (Integer.parseInt(qty) > laptop.getInStockNumber()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/laptopDetail?id="+laptop.getId();
		}
		
		CartItem cartItem = cartItemService.addLaptopToCartItem(laptop, user, Integer.parseInt(qty));
		model.addAttribute("addLaptopSuccess", true);
		
		return "forward:/laptopDetail?id="+laptop.getId();
	}
	
	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(
			@ModelAttribute("id") Long cartItemId,
			@ModelAttribute("qty") int qty
			) {
		CartItem cartItem = cartItemService.findById(cartItemId);
		cartItem.setQty(qty);
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(
			@RequestParam("id") Long id
			) {
		cartItemService.removeCartItem(cartItemService.findById(id));
		
		return "forward:/shoppingCart/cart";
	}
}
