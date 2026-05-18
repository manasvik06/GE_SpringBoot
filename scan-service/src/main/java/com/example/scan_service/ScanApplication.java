package com.example.scan_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main entry point of our Spring Boot microservice.
 *
 * @SpringBootApplication does three things automatically:
 *  1. Scans all classes in this package for Spring annotations
 *  2. Auto-configures Spring based on our dependencies in pom.xml
 *  3. Marks this as a configuration class
 *
 *
 * Compared to our stage 1 (monolothic) application where everything was done
 * in a giant main() method, this version all the setup is handled by the
 * SpringApplication.run() line
 *
 * When you run this, Spring Boot:
 *  - Starts an embedded web server (Tomcat) on port 8080
 *  - Registers all our REST endpoints automatically
 *  - Manages all our services and dependencies
 */
@SpringBootApplication
public class ScanApplication {
	public static void main(String[] args) {
		//one line to start our microservice
		SpringApplication.run(ScanApplication.class, args);
	}
}