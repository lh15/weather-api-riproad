package com.leibel.weatherapi.repository;

import com.leibel.weatherapi.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<Location, Long> {
	Location findByLatitudeAndLongitude(double lat, double lng);
}
