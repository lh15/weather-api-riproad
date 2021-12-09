package com.leibel.weatherapi.controller;

import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegistrationController {

	private final UserService userService;

	@PostMapping("/users/create")
	public String registerUser(@RequestParam(value = "username", defaultValue = "leibel") String username) {
		User user = userService.register(username);
		if(user == null){
			return "User already exists";
		}
		return user.toString();
	}



}
