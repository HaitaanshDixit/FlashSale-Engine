package com.flashsale.flashsale_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class FlashsaleEngineApplication {
	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(FlashsaleEngineApplication.class, args);
		System.out.println("Server has started!");
	}

}
