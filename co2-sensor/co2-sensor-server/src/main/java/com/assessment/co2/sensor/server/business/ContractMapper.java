package com.assessment.co2.sensor.server.business;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.server.contract.GetAlerts;
import com.assessment.co2.sensor.server.contract.GetSensorMetrics;
import com.assessment.co2.sensor.server.contract.GetStatus;
import com.assessment.co2.sensor.server.contract.Mesurment;

public interface ContractMapper {

	/**
	 * Maps Domain Metrics to getSensorMetrics
	 * 
	 * @param metrics
	 * @return
	 */
	GetSensorMetrics convertSummarizedSensorDayToGetSensonMetrics(SummarizedSensorDay metrics);

	/**
	 * Maps domain alerts to GetAlerts
	 * 
	 * @param alerts
	 * @return
	 */
	GetAlerts convertSensorStatusToGetAlert(SensorStatus sensorStatus);

	/**
	 * Convert domain status to getStatus
	 * 
	 * @param status
	 * @return
	 */
	GetStatus convertStatusToGetStatus(Status status);

	/**
	 * Convert measurement to Sensor
	 * 
	 * @param measurement
	 * @return
	 */
	Sensor convertMeasurementToSensorStatus(Mesurment measurement);

}