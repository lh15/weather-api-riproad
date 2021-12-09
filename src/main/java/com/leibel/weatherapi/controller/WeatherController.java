package com.leibel.weatherapi.controller;

import com.google.maps.errors.ApiException;
import com.leibel.weatherapi.model.Location;
import com.leibel.weatherapi.repository.LocationRepository;
import com.leibel.weatherapi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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


	private final String apiBase = "http://api.openweathermap.org/data/2.5/weather?q=";
	private final String apiForecast = "http://api.openweathermap.org/data/2.5/forecast?q=";
	private final String units = "imperial"; // metric
	private final String lang = "en";
	private final String apiKey = "e3410a808ad9587ec01a3c7c32f13d2c";

	/*
	* 1. Client should have a list of locations for the user from weather/locations GET
	* 2. If not, they can pass any part of the address and we'll hit up Google Maps to get the Lat and Long
	*
	* */
	@GetMapping("get")
	public String getLocationWeather(@RequestParam(value = "locId", defaultValue = "0") long locationID, @RequestParam(value = "address", defaultValue = "") String address) throws IOException, InterruptedException, ApiException {
		Location location;
		if(locationID > 0){
			location = locationService.getById(locationID);
		} else {
			location = locationService.getLocationByAddress(address);
		}
		HttpClient client = HttpClient.newHttpClient();
		double latt = 33.8787116;//location.getLatitude()
		double lngg = -118.3666236; //location.getLongitude()
		String apiUrl = apiBase + "&lat=" + latt + "&lon=" + lngg + "&appid=" + apiKey + "&mode=json&units=" + units + "&lang=" + lang;

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(apiUrl))
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		JSONObject obj = new org.json.JSONObject(response);
		System.out.println(response.body());

		//TODO: Return specific fields JSON
		/*
		*{
			"coord": {
				"lon": -118.3666,
				"lat": 33.8787
			},
			"weather": [{
				"id": 804,
				"main": "Clouds",
				"description": "overcast clouds",
				"icon": "04n"
			}],
			"base": "stations",
			"main": {
				"temp": 57.18,
				"feels_like": 56.88,
				"temp_min": 53.69,
				"temp_max": 60.28,
				"pressure": 1015,
				"humidity": 91
			},
			"visibility": 10000,
			"wind": {
				"speed": 3,
				"deg": 241,
				"gust": 5.99
			},
			"clouds": {
				"all": 90
			},
			"dt": 1639034052,
			"sys": {
				"type": 2,
				"id": 2010405,
				"country": "US",
				"sunrise": 1638974800,
				"sunset": 1639010684
			},
			"timezone": -28800,
			"id": 5365603,
			"name": "Lawndale",
			"cod": 200
		}
		*
		*
		* */
//		JSONObject returnJson = new JSONObject();
//		returnJson.put("location_name", obj.get("name"));
//		returnJson.put("weather_description", obj.get("weather").get("description"));

		return obj.toString();
	}

}
