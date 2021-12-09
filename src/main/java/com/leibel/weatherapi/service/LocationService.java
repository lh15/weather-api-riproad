package com.leibel.weatherapi.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.leibel.weatherapi.model.Location;
import com.leibel.weatherapi.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class LocationService {

    //Initialize Google Maps Geocoding Api
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("--AIzaSyBzrx_jFh5OhSwu7CgVxhj8_Ew1KC1AxLU--")
            .build();

    private final LocationRepository locationRepository;

    public Location getLocationByAddress(String address) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GeocodingResult result = results[0];
        double lat = result.geometry.location.lat;
        double lng = result.geometry.location.lng;
        Location location  = locationRepository.findByLatitudeAndLongitude(lat, lng);
        if(location != null){
            return location;
        }
        location = new Location();
        location.setLatitude(lat);
        location.setLongitude(lng);
        return locationRepository.save(location);
    }

    public Location getById(long id){
        return locationRepository.getById(id);
    }

}
