package com.laptopstore.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.LaptopToCartItem;
import com.laptopstore.domain.Order;
import com.laptopstore.domain.ShoppingCart;
import com.laptopstore.domain.User;
import com.laptopstore.repository.CartItemRepository;
import com.laptopstore.repository.LaptopToCartItemRepository;
import com.laptopstore.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private LaptopToCartItemRepository laptopToCartItemRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart){
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}
	
	public CartItem updateCartItem(CartItem cartItem) {
		BigDecimal bigDecimal = new BigDecimal(cartItem.getLaptop().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		//bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		cartItemRepository.save(cartItem);
		return cartItem;
	}
	
	public CartItem addLaptopToCartItem(Laptop laptop, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for(CartItem cartItem : cartItemList) {
			if(laptop.getId() == cartItem.getLaptop().getId()) {
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubtotal(new BigDecimal(laptop.getOurPrice()).multiply(new BigDecimal(cartItem.getQty())));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setLaptop(laptop);
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(laptop.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);

		LaptopToCartItem laptopToCartItem =  new LaptopToCartItem();
		laptopToCartItem.setLaptop(laptop);
		laptopToCartItem.setCartItem(cartItem);
		laptopToCartItemRepository.save(laptopToCartItem);
		
		return cartItem;
		
	}
	
	public CartItem findById(Long id) {
		return cartItemRepository.findById(id).get();
	}
	
	public void removeCartItem(CartItem cartItem) {
		laptopToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}
	
	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
	
	public List<CartItem> findByOrder(Order order){
		return cartItemRepository.findByOrder(order);
	}
}
