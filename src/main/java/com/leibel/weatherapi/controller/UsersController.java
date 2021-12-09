package com.leibel.weatherapi.controller;

import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

	private final UserService userService;

	@PostMapping("create")
	public ResponseEntity registerUser(@RequestParam(value = "username", defaultValue = "leibel") String username) {
		User user = userService.register(username);
		if(user == null){
			return new ResponseEntity<>("User Already Exists", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("login")
	public ResponseEntity loginUser(@RequestParam(value = "username") String username) {
		User user = userService.login(username);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
