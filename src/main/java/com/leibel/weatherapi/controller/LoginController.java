package com.leibel.weatherapi.controller;

import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

	private final UserService userService;

	@PostMapping("/users/login")
	public String loginUser(@RequestParam(value = "username") String username) {
		User user = userService.login(username);
		return user.toString();
	}

}
