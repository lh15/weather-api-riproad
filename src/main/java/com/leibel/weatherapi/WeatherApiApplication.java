package com.leibel.weatherapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WeatherApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(WeatherApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

    public static void log(String message){
        logger.debug(message);
    }

}
