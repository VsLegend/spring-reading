package com.example.springreading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = {"com.example.springreading.scanPackages",})
@SpringBootApplication(scanBasePackages = {"com.example.springreading",})
public class SpringReadingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringReadingApplication.class, args);
	}

}
