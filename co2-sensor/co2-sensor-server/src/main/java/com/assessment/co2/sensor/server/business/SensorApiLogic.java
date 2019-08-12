package com.assessment.co2.sensor.server.business;

import org.springframework.http.ResponseEntity;

import com.assessment.co2.sensor.server.contract.GetAlerts;
import com.assessment.co2.sensor.server.contract.GetSensorMetrics;
import com.assessment.co2.sensor.server.contract.GetStatus;
import com.assessment.co2.sensor.server.contract.Mesurment;

public interface SensorApiLogic {

	/**
	 * Saves sensor level
	 * 
	 * @param sensorId
	 * @param measurement
	 * @return
	 */
	ResponseEntity<Void> saveSensorLevel(String sensorId, Mesurment measurement);

	/**
	 * Get current sensor status from sensor manager maps to contract status and
	 * sends the response to controller
	 * 
	 * @param id
	 * @return
	 */
	ResponseEntity<GetStatus> getSensorStatus(String id);

	/**
	 * Get sensorMetrics from sensormanager and maps model object to contract object
	 * 
	 * @param sensorId
	 * @return
	 */
	ResponseEntity<GetSensorMetrics> getSensorMetrics(String sensorId);

	/**
	 * Gets alerts from sensor manager and maps model object to contract
	 * 
	 * @param sensorId
	 * @return
	 */
	ResponseEntity<GetAlerts> getAlerts(String sensorId);

}