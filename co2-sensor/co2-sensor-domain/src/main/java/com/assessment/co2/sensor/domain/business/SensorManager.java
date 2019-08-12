package com.assessment.co2.sensor.domain.business;

import java.util.UUID;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
/**
 * Interface for Sensor manager
 * @author ghosh
 *
 */
public interface SensorManager {
	void saveSensor(Sensor sensor);
	Status getSensorStatus(UUID uuid) throws Exception;
	SummarizedSensorDay getSensorMetrics(UUID uuid) throws Exception;
	SensorStatus getAlerts(UUID uuid) throws Exception;
	void getDailySummarizedDaySensorData();
}
