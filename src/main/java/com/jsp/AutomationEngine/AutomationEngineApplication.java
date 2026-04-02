package com.jsp.AutomationEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = "org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration")
public class AutomationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomationEngineApplication.class, args);
	}

}
