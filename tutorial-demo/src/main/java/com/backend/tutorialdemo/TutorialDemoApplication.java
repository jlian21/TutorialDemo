package com.backend.tutorialdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class TutorialDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialDemoApplication.class, args);
	}

}
