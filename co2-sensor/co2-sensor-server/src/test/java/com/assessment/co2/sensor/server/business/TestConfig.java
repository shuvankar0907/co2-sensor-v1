package com.assessment.co2.sensor.server.business;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
//@ComponentScan({"com.assessment.co2.sensor.domain" })
@ComponentScan(basePackages={"com.assessment.co2.sensor.domain","com.assessment.co2.sensor.server"})
public class TestConfig {

}
