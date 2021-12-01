package com.laptopstore.service;

import java.util.List;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.Order;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addLaptopToCartItem(Laptop laptop, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);
}
