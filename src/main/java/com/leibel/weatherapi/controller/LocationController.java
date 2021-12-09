package com.leibel.weatherapi.controller;

import com.google.maps.errors.ApiException;
import com.leibel.weatherapi.model.Location;
import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.service.LocationService;
import com.leibel.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {

	private final UserService userService;
	private final LocationService locationService;

	@GetMapping("weather/locations")
	public String getUserLocations(@RequestParam(value = "username", defaultValue = "") String username) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return "REQUIRE_LOGIN";
		}
		Set<Location> locations = user.getLocations();
		//TODO: Send JSON
		//QUESTION: Should the list api return the weather for each location in the list?
		return locations.toString();
	}

	@PostMapping("weather/locations/add")
	public String addUserLocation(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "address", defaultValue = "") String address) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return "REQUIRE_LOGIN";
		}
		Location location = locationService.getLocationByAddress(address);
		Set<Location> locations = user.getLocations();
		locations.add(location);
		user.setLocations(locations);
		userService.save(user);
		//TODO: Send JSON
		return "Location Added";
	}

	@DeleteMapping("weather/locations/delete")
	public String removeUserLocation(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "locId") long locationID) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return "REQUIRE_LOGIN";
		}
		Location location = locationService.getById(locationID);
		Set<Location> locations = user.getLocations();
		locations.remove(location);
		user.setLocations(locations);
		userService.save(user);
		//TODO: Send JSON
		return "Location Removed";
	}

}
