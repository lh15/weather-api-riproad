package com.leibel.weatherapi.controller;

import com.google.maps.errors.ApiException;
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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherController {

	private final LocationService locationService;
	private final UserService userService;


	private final String apiBase = "http://api.openweathermap.org/data/2.5/weather?q=";
	private final String apiForecast = "http://api.openweathermap.org/data/2.5/forecast?q=";
	private final String units = "imperial"; // metric
	private final String lang = "en";
	private final String apiKey = "KEY";

	/*
	* 1. Client should have a list of locations for the user from weather/locations GET
	* 2. If not, they can pass any part of the address and we'll hit up Google Maps to get the Lat and Long
	*
	* */
	@GetMapping("get")
	public ResponseEntity getLocationWeather(@RequestParam(value = "locId", defaultValue = "0") long locationID, @RequestParam(value = "address", defaultValue = "") String address, @RequestParam(value = "username", defaultValue = "") String username) throws IOException, InterruptedException, ApiException {
		User user = userService.login(username);
		if(user == null){
			return new ResponseEntity<>("LOGIN", HttpStatus.FORBIDDEN);
		}
		Location location;
		if(locationID > 0){
			location = locationService.getById(locationID);
		} else {
			location = locationService.getLocationByAddress(address);
		}
		HttpClient client = HttpClient.newHttpClient();
		double latt = 33.8787116;//location.getLatitude()
		double lngg = -118.3666236; //location.getLongitude()

		//Get Current Weather
		String apiUrlCurrWeather = apiBase + "&lat=" + latt + "&lon=" + lngg + "&appid=" + apiKey + "&mode=json&units=" + units + "&lang=" + lang;
		HttpRequest requestCurrWeather = HttpRequest.newBuilder()
				.uri(URI.create(apiUrlCurrWeather))
				.build();
		HttpResponse<String> currWeather = client.send(requestCurrWeather, HttpResponse.BodyHandlers.ofString());
		JSONObject currWeatherObj = new org.json.JSONObject(currWeather);

		//Get Forecast
		String apiUrlWeatherForcast = apiForecast + "&lat=" + latt + "&lon=" + lngg + "&appid=" + apiKey + "&mode=json&units=" + units + "&lang=" + lang;
		HttpRequest requestWeatherForecast = HttpRequest.newBuilder()
				.uri(URI.create(apiUrlWeatherForcast))
				.build();
		HttpResponse<String> weatherForecast = client.send(requestWeatherForecast, HttpResponse.BodyHandlers.ofString());
		JSONObject weatherForecastObj = new org.json.JSONObject(weatherForecast);

//		System.out.println(response.body());

		JSONObject response = new JSONObject();

		//TODO: Return specific fields JSON
		response.put("current_weather", currWeatherObj);
		response.put("forecast", weatherForecastObj);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
