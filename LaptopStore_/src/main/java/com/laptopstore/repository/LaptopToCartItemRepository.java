package com.laptopstore.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.LaptopToCartItem;

@Transactional
public interface LaptopToCartItemRepository extends CrudRepository<LaptopToCartItem, Long>{

	void deleteByCartItem(CartItem cartItem);
}
