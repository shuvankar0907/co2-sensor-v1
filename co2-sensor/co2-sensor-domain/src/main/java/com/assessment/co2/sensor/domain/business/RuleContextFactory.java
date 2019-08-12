/**
 * 
 */
package com.assessment.co2.sensor.domain.business;

import com.assessment.co2.sensor.domain.model.Sensor;

/**
 * Interface for rule context factory, Class which implements this inteface is
 * responsible for creating the Rule context
 * 
 * @author ghosh
 *
 */
public interface RuleContextFactory {
	RuleContext getRuleContext(Sensor sensorData);

}
