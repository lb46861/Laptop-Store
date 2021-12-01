package com.laptopstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.laptopstore.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
