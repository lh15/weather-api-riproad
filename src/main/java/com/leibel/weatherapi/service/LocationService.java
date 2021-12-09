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
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class LocationService {

    //Initialize Google Maps Geocoding Api
    GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("KEY")
            .build();

    private final LocationRepository locationRepository;

    public Location getLocationByAddress(String address) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        GeocodingResult result = results[0];
        double lat = result.geometry.location.lat;
        double lng = result.geometry.location.lng;
        return getOrCreateLocation(lat, lng);
    }

    public List<JSONObject> getResultsByAddress(String address) throws IOException, InterruptedException, ApiException {
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                address).await();
        List<JSONObject> resultsObjs = new ArrayList<>();
        for (GeocodingResult result: results) {
            JSONObject jObj = new JSONObject();
            double lat = result.geometry.location.lat;
            double lng = result.geometry.location.lng;
            jObj.put("lat", lat);
            jObj.put("lng", lng);
            jObj.put("google_placeid", result.placeId);
            jObj.put("formatted_address", result.formattedAddress);
            resultsObjs.add(jObj);
        }
        return resultsObjs;
    }

    public Location getById(long id){
        return locationRepository.getById(id);
    }

    public Location getByCoordinates(double lat, double lng){
        return getOrCreateLocation(lat, lng);
    }

    private Location getOrCreateLocation(double lat, double lng) {
        Location location  = locationRepository.findByLatitudeAndLongitude(lat, lng);
        if(location != null){
            return location;
        }
        location = new Location();
        location.setLatitude(lat);
        location.setLongitude(lng);
        return locationRepository.save(location);
    }

}
