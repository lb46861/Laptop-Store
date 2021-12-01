package com.laptopstore.service.impl;

import org.springframework.stereotype.Service;

import com.laptopstore.domain.ShippingAddress;
import com.laptopstore.domain.UserShipping;
import com.laptopstore.service.ShippingAddressService;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService{

	public ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress) {
		shippingAddress.setShippingAddressName(userShipping.getUserShippingName());
		shippingAddress.setShippingAddressStreet1(userShipping.getUserShippingStreet1());
		shippingAddress.setShippingAddressStreet2(userShipping.getUserShippingStreet2());
		shippingAddress.setShippingAddressCountry(userShipping.getUserShippingCountry());
		shippingAddress.setShippingAddressCity(userShipping.getUserShippingCity());
		shippingAddress.setShippingAddressZipcode(userShipping.getUserShippingZipcode());
		
		return shippingAddress;
	}
}
