package com.laptopstore.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Order;
import com.laptopstore.domain.ShippingAddress;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;
import com.laptopstore.domain.UserShipping;
import com.laptopstore.service.CartItemService;
import com.laptopstore.service.OrderService;
import com.laptopstore.service.ShippingAddressService;
import com.laptopstore.service.ShoppingCartService;
import com.laptopstore.service.UserService;
import com.laptopstore.service.UserShippingService;
import com.laptopstore.utility.MailConstructor;

@Controller
public class CheckoutController {
	
	private ShippingAddress shippingAddress = new ShippingAddress();

	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value="missingRequiredField", required=false) boolean missingRequiredField,
			Model model, Principal principal
			) {
			User user = userService.findByUsername(principal.getName());
			
			if(cartId != user.getShoppingCart().getId()) {
				return "badRequestPage";
			}
			
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			
			if(cartItemList.size() == 0) {
				model.addAttribute("emptyCart", true);
				return "forward:/shoppingCart/cart";
			}
			
			for(CartItem cartItem : cartItemList) {
				if (cartItem.getLaptop().getInStockNumber() < cartItem.getQty()) {
					model.addAttribute("notEnoughStock", true);
					return "forward:/shoppingCart/cart";
				}
			}
			
			List<UserShipping> userShippingList = user.getUserShippingList();
			
			model.addAttribute("userShippingList", userShippingList);
			
			if(userShippingList.size() == 0) {
				model.addAttribute("emptyShippingList", true);
			}
			else {
				model.addAttribute("emptyShippingList", false);
			}
			
			ShoppingCart shoppingCart = user.getShoppingCart();
			
			for(UserShipping userShipping : userShippingList) {
				if(userShipping.isUserShippingDefault()) {
					shippingAddressService.setByUserShipping(userShipping, shippingAddress);
				}
			}
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", shoppingCart);
			
			model.addAttribute("classActiveShipping", true);
			
			if(missingRequiredField) {
				model.addAttribute("missingRequiredField", true);
			}
			
			return "checkout";		
	}
	
	@RequestMapping(value="/checkout", method=RequestMethod.POST)
	public String checkoutPost(
			@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			Model model, Principal principal
			) {
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);
		
		if(
				shippingAddress.getShippingAddressName().isEmpty() ||
				shippingAddress.getShippingAddressStreet1().isEmpty() ||
				shippingAddress.getShippingAddressCountry().isEmpty() ||
				shippingAddress.getShippingAddressCity().isEmpty() ||
				shippingAddress.getShippingAddressZipcode().isEmpty()
				) {
			return "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";			
		}
		User user = userService.findByUsername(principal.getName());
		
		Order order = orderService.createOrder(shoppingCart, shippingAddress, user);
		
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		
		estimatedDeliveryDate = today.plusDays(5);
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
	}
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(
			@RequestParam("userShippingId") Long userShippingId,
			Model model, Principal principal
			) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(userShipping.getUser().getId() != user.getId()) {
			return "badRequestPage";
		}
		else {
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", user.getShoppingCart());
					
			List<UserShipping> userShippingList = user.getUserShippingList();
			
			model.addAttribute("userShippingList", userShippingList);
			
			model.addAttribute("shippingAddress", shippingAddress);
			
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("emptyShippingList", false);
			
			return "checkout";
			
		}
	}
}
	