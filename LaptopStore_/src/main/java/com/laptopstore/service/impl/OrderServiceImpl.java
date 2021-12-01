package com.laptopstore.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.Order;
import com.laptopstore.domain.ShippingAddress;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;
import com.laptopstore.repository.OrderRepository;
import com.laptopstore.service.CartItemService;
import com.laptopstore.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	public synchronized Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, User user) {
		Order order = new Order();
		order.setOrderStatus("created");
		order.setShippingAddress(shippingAddress);
		
		List <CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for(CartItem cartItem : cartItemList) {
			Laptop laptop = cartItem.getLaptop();
			cartItem.setOrder(order);
			laptop.setInStockNumber(laptop.getInStockNumber() - cartItem.getQty());
		}
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		shippingAddress.setOrder(order);
		order.setUser(user);
		order = orderRepository.save(order);
		
		return order;
	}
	
	public Order findById(Long id) {
		return orderRepository.findById(id).get();
	}
}
