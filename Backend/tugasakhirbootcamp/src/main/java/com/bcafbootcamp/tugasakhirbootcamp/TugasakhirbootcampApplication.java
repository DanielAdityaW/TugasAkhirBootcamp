package com.bcafbootcamp.tugasakhirbootcamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class TugasakhirbootcampApplication {

	public static void main(String[] args) {
		SpringApplication.run(TugasakhirbootcampApplication.class, args);
	}

	@Bean	
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
