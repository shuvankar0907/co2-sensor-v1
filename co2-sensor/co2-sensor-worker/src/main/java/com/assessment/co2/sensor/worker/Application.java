/**
 * 
 */
package com.assessment.co2.sensor.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author ghosh
 *
 */
@SpringBootApplication(scanBasePackages = {"com.assessment.co2.sensor.domain","com.assessment.co2.sensor.worker"})
public class Application extends SpringBootServletInitializer{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}
