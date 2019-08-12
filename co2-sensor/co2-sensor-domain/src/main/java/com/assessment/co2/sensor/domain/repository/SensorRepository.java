package com.assessment.co2.sensor.domain.repository;


import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
/**
 * Sensor repository
 * @author ghosh
 *
 */
public interface SensorRepository {
	void saveLevel(Sensor sensor);
	List<Sensor> getPreviousXLevels(UUID sensorId,int perviousRecords);
	List<SummarizedSensorDay> getAggregatedData(DateTime startDate, DateTime endDate);
	void delete(DateTime startDate, DateTime endDate);
	
}
