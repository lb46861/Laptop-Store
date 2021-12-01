package com.laptopstore.service;

import java.util.Set;

import com.laptopstore.domain.User;
import com.laptopstore.domain.UserShipping;
import com.laptopstore.domain.security.PasswordResetToken;
import com.laptopstore.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User findById(Long id);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);
}
