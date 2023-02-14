package com.example.springreading;

import com.example.springreading.service.BusinessService;
import com.example.springreading.service.BusinessServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringReadingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReadingApplication.class, args);
		BusinessServiceImpl businessService = new BusinessServiceImpl(BusinessServiceImpl.class.getName());
		System.out.println(businessService.getName());
	}

}
