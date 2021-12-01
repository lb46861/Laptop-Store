package com.laptopstore.service;

import com.laptopstore.domain.ShippingAddress;
import com.laptopstore.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
