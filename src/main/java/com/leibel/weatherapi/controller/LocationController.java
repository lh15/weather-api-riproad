package com.leibel.weatherapi.controller;

import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.leibel.weatherapi.model.Location;
import com.leibel.weatherapi.model.User;
import com.leibel.weatherapi.service.LocationService;
import com.leibel.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/locations")
public class LocationController {

	private final UserService userService;
	private final LocationService locationService;

	@GetMapping("/")
	public ResponseEntity getUserLocations(@RequestParam(value = "username", defaultValue = "") String username) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return new ResponseEntity<>("LOGIN", HttpStatus.FORBIDDEN);
		}
		Set<Location> locations = user.getLocations();
		List<JSONObject> resultsObjs = new ArrayList<>();
		for (Location location: locations) {
			JSONObject jObj = new JSONObject();
			jObj.put("lat", location.getLatitude());
			jObj.put("lng", location.getLongitude());
			jObj.put("name", location.getName());
			jObj.put("id", location.getId());
			resultsObjs.add(jObj);
		}
		//TODO: THE list api should really return the weather for each location in the list?
		return new ResponseEntity<>(resultsObjs, HttpStatus.OK);
	}

	@GetMapping("search")
	public ResponseEntity searchLocations(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "search", defaultValue = "") String search) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return new ResponseEntity<>("LOGIN", HttpStatus.FORBIDDEN);
		}
		List<JSONObject> results = locationService.getResultsByAddress(search);
		return new ResponseEntity<>(results, HttpStatus.OK);
	}

	@PostMapping("add")
	public ResponseEntity addUserLocation(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "address", defaultValue = "") String address, @RequestParam(value = "location_name", defaultValue = "") String locationName,  @RequestParam(value = "lat", defaultValue = "") double lat,  @RequestParam(value = "lng", defaultValue = "") double lng) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return new ResponseEntity<>("LOGIN", HttpStatus.FORBIDDEN);
		}
		//User can either add by coordinates or by address, in which case we will hit Google Maps Api
		Location location;
		if(lat != 0 && lng != 0){
			location = locationService.getByCoordinates(lat, lng);
		} else {
			location = locationService.getLocationByAddress(address);
		}
		Set<Location> locations = user.getLocations();
		locations.add(location);
		user.setLocations(locations);
		userService.save(user);
		return new ResponseEntity<>("Location Saved", HttpStatus.ACCEPTED);
	}

	@DeleteMapping("delete")
	public ResponseEntity removeUserLocation(@RequestParam(value = "username", defaultValue = "") String username, @RequestParam(value = "locId") long locationID) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return new ResponseEntity<>("LOGIN", HttpStatus.FORBIDDEN);
		}
		Location location = locationService.getById(locationID);
		Set<Location> locations = user.getLocations();
		locations.remove(location);
		user.setLocations(locations);
		userService.save(user);
		return new ResponseEntity<>("Location Deleted", HttpStatus.ACCEPTED);
	}

}
