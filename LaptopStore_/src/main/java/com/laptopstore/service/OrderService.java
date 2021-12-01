package com.laptopstore.service;

import com.laptopstore.domain.Order;
import com.laptopstore.domain.ShippingAddress;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, User user);
	
	Order findById(Long id);
}
