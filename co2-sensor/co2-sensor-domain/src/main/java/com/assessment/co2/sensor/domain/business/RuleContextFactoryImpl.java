/**
 * 
 */
package com.assessment.co2.sensor.domain.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;

/**
 * This class is responsible for creating RuleContext object
 * @author ghosh
 *
 */
@Component
public class RuleContextFactoryImpl implements RuleContextFactory{
	
	SensorStatusRepository sensorStatusRepository;
	@Autowired
	public RuleContextFactoryImpl(SensorStatusRepository sensorStatusRepository) {
		this.sensorStatusRepository=sensorStatusRepository;
	}

	@Override
	public RuleContext getRuleContext(Sensor sensorData) {
		return new RuleContext(sensorData, sensorStatusRepository);
	}

}
