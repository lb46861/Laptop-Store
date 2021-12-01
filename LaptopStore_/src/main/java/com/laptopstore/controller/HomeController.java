package com.laptopstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laptopstore.domain.CartItem;
import com.laptopstore.domain.Laptop;
import com.laptopstore.domain.Order;
import com.laptopstore.domain.User;
import com.laptopstore.domain.UserShipping;
import com.laptopstore.domain.security.PasswordResetToken;
import com.laptopstore.domain.security.Role;
import com.laptopstore.domain.security.UserRole;
import com.laptopstore.service.CartItemService;
import com.laptopstore.service.LaptopService;
import com.laptopstore.service.OrderService;
import com.laptopstore.service.UserService;
import com.laptopstore.service.UserShippingService;
import com.laptopstore.service.impl.UserSecurityService;
import com.laptopstore.utility.MailConstructor;
import com.laptopstore.utility.SecurityUtility;


@Controller
public class HomeController {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private LaptopService laptopService;
	
	@Autowired
	private UserShippingService userShippingService;
	

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping("/laptopview")
	public String laptopview(
			Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		List<Laptop> laptopList = laptopService.findAll();
		model.addAttribute("laptopList", laptopList);
		model.addAttribute("activeAll", true);
		return "laptopview";
				
	}
	
	@RequestMapping("/laptopDetail")
	public String laptopDetail(
			@PathParam("id") Long id, Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Laptop laptop = laptopService.findById(id);
		
		model.addAttribute("laptop", laptop);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		
		return "laptopDetail";
	}
	
	@RequestMapping("/forgetPassword")
	public String forgetPassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Model model
			) {
		
		model.addAttribute("classActiveForgetPassword", true);
		
		User user = userService.findByEmail(email);
		
		if(user == null) {
			model.addAttribute("emailNotExists", true);			
			return "myAccount";
		}
				
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);			
		userService.save(user);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);		
		String appUrl="http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();		
		SimpleMailMessage newEmail = mailConstructor.forgetPasswordTokenEmail(appUrl, request.getLocale(), token, user, password);		
		mailSender.send(newEmail);		
		model.addAttribute("forgetPasswordEmailSent", "true");
		
		return "myAccount";

	}
	
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("listOfShippingAddresses", true);
				
		model.addAttribute("classActiveEdit", true);		
		return "myProfile";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(
			Model model, Principal principal, HttpServletRequest request
			) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("user", user);
		model.addAttribute("userShippingList", user.getUserShippingList());		
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";

	}
	

	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(
			Model model, Principal principal
			){
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@RequestMapping(value="/addNewShippingAddress", method=RequestMethod.POST)
	public String addNewShippingAddressPost(
			@ModelAttribute("userShipping") UserShipping userShipping,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		model.addAttribute("classActiveShipping", true);				
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
	}
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		}
		else {
			model.addAttribute("user", user);
			model.addAttribute("userShipping", userShipping);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			return "myProfile";
		}
	}
	
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("orderList", user.getOrderList());
		return "myProfile";
	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			return "myProfile";
		}
	}
	
	

	
	@RequestMapping(value="/newUser", method=RequestMethod.POST)
	public String newUserPost(
		HttpServletRequest request,
		@ModelAttribute("email") String userEmail,
		@ModelAttribute("username") String username,
		Model model
		) throws Exception{
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		
		if(userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);			
			return "myAccount";
		}
		
		if(userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);			
			return "myAccount";
		}
		User user = new User();
		user.setUsername(username);
		user.setEmail(userEmail);
		
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl="http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		
		mailSender.send(email);
		
		model.addAttribute("emailSent", "true");
		model.addAttribute("orderList", user.getOrderList());
		return "myAccount";
		
	}
	
	
	@RequestMapping("/newUser")
	public String newUser(
			Locale locale,
			@RequestParam("token") String token,
			Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		if(passToken == null) {
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "badRequestPage";
		}
		User user = passToken.getUser();
		String username = user.getUsername();
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("user", user);
		
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}
	
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public String updateUserInfo(
			@ModelAttribute("user") User user,
			@ModelAttribute("newPassword") String newPassword,
			Model model
			) throws Exception {
		User currentUser = userService.findById(user.getId());
		
		if(currentUser == null) {
			throw new Exception ("User not found");
		}
		
		BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
		String currUserPass = currentUser.getPassword();
		if(passwordEncoder.matches(user.getPassword(), currUserPass)){
			
			
			//check email already exists
			if (userService.findByEmail(user.getEmail())!=null) {
				if(userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
					model.addAttribute("emailExists", true);
					model.addAttribute("classActiveEdit", true);
					return "myProfile";
				}
			}
			
			//check username already exists
			if (userService.findByUsername(user.getUsername())!=null) {
				if(userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
					model.addAttribute("usernameExists", true);
					model.addAttribute("classActiveEdit", true);
					return "myProfile";
				}
			}
						
			//update password
			if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
				currentUser.setPassword(passwordEncoder.encode(newPassword));
			}

			
			currentUser.setFirstName(user.getFirstName());
			currentUser.setLastName(user.getLastName());
			currentUser.setUsername(user.getUsername());
			currentUser.setEmail(user.getEmail());
			
			userService.save(currentUser);
			
			model.addAttribute("updateSuccess", true);
			model.addAttribute("user", currentUser);
			model.addAttribute("classActiveEdit", true);
			
			model.addAttribute("listOfShippingAddresses", true);

			
			UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getUsername());

			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			model.addAttribute("orderList", user.getOrderList());
			return "myProfile";
		}

		else {
			model.addAttribute("incorrectPassword", true);		
			model.addAttribute("classActiveEdit", true);
			return "myProfile";
		}
	}
	
	@RequestMapping("/orderDetail")
	public String orderDetail(
			@RequestParam("id") Long orderId,
			Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		Order order = orderService.findById(orderId);
		
		if(order.getUser().getId()!=user.getId()) {
			return "badRequestPage";
		} else {
			List<CartItem> cartItemList = cartItemService.findByOrder(order);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("user", user);
			model.addAttribute("order", order);
			
			model.addAttribute("userShippingList", user.getUserShippingList());
			model.addAttribute("orderList", user.getOrderList());
			
			UserShipping userShipping = new UserShipping();
			model.addAttribute("userShipping", userShipping);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveOrders", true);
			model.addAttribute("displayOrderDetail", true);
			
			return "myProfile";
		}
	}

}
