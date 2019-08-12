package com.assessment.co2.sensor.domain.business;

import com.assessment.co2.sensor.domain.model.Sensor;
/**
 * Interface for rule. Rule definition class implements this interface.
 * @author ghosh
 *
 */
public interface Rule {

	/**
	 * Based on the values of the previous reading current status is calculated
	 * 
	 * @param context
	 * @return
	 */
	RuleContext executeRule(Sensor newSensor);

}