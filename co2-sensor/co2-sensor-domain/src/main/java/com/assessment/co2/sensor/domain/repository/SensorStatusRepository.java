package com.assessment.co2.sensor.domain.repository;

import java.util.UUID;

import com.assessment.co2.sensor.domain.model.SensorStatus;

/**
 * 
 * @author ghosh
 *
 */
public interface SensorStatusRepository {
	void save(SensorStatus sensorStatus);
	SensorStatus getSensorStatus(UUID sensorId);
}
