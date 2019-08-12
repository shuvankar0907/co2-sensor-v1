package com.assessment.co2.sensor.domain.business;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.domain.repository.SensorRepository;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;
import com.assessment.co2.sensor.domain.repository.SummarizedSensorDayReposirory;

/**
 * This is the manager class for all sensor data.
 * 
 * @author ghosh
 *
 */
@Component("SensorManagerImpl")
public class SensorManagerImpl implements SensorManager {
	//private AlertRepository alertRepository;
	private SensorRepository sensorRepository;
	private SummarizedSensorDayReposirory summarizedSensorDayRepository;
	private SensorStatusRepository sensorStatusRepository;
	private Rule rule;
	/**
	 * Constructor for injecting the dependencies
	 * @param alertRepository
	 * @param sensorRepository
	 * @param summarizedSensorDayRepository
	 * @param sensorStatusRepository
	 */
	@Autowired
	public SensorManagerImpl(Rule rule, SensorRepository sensorRepository,
			SummarizedSensorDayReposirory summarizedSensorDayRepository,
			SensorStatusRepository sensorStatusRepository) {
		this.rule = rule;
		this.sensorRepository = sensorRepository;
		this.summarizedSensorDayRepository = summarizedSensorDayRepository;
		this.sensorStatusRepository = sensorStatusRepository;
	}
	/**
	 * This method saves new sensor data 
	 */
	public void saveSensor(Sensor sensor) {
		sensor.setId(UUID.randomUUID());
		RuleContext ruleContext= rule.executeRule(sensor);
		sensorRepository.saveLevel(ruleContext.getNewSensor());
		sensorStatusRepository.save(ruleContext.getNewSensorStatus());
	}
	
	/**
	 * This method returns current status of the sensor
	 */
	public Status getSensorStatus(UUID sensorUUId) throws Exception {
		return sensorStatusRepository.getSensorStatus(sensorUUId).getStatus();
	}

	/**
	 * This method gets 30 days metrics for a sensor. It calculates 30days by
	 * today's date+1 (start of the day)minus 30
	 */
	public SummarizedSensorDay getSensorMetrics(UUID sensorId) throws Exception {
		DateTime endDate = LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime startDate = LocalDate.now().minusDays(30).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		SummarizedSensorDay summarizedDays = summarizedSensorDayRepository.getRangeOfdaysSummarizedMetrix(sensorId,
				startDate, endDate);
		
		return summarizedDays;
	}
	/**
	 * Returns all the alerts
	 */
	public SensorStatus getAlerts(UUID sensorId) throws Exception {
		return sensorStatusRepository.getSensorStatus(sensorId);

	}
	/**
	 * Gets yesterday's summarised data from sensor and saves it in SummarizedSensorDay collection.
	 * It also deletes the same range of data from sensor collection
	 */
	public void getDailySummarizedDaySensorData() {
		DateTime todaysStartDate = LocalDate.now().minusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime todaysEndDate = LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());

		List<SummarizedSensorDay> summarizedDaysDatas = sensorRepository.getAggregatedData(todaysStartDate,
				todaysEndDate);
		if (summarizedDaysDatas != null && summarizedDaysDatas.size() > 0) {
			summarizedSensorDayRepository.saveAllSummarizedSensorDayRepository(summarizedDaysDatas);
			sensorRepository.delete(todaysStartDate, todaysEndDate);
		}
	}

}
