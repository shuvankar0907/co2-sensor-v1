package com.assessment.co2.sensor.server.business;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.server.contract.GetAlerts;
import com.assessment.co2.sensor.server.contract.GetSensorMetrics;
import com.assessment.co2.sensor.server.contract.GetStatus;
import com.assessment.co2.sensor.server.contract.Mesurment;

/**
 * This Class maps the domain model to contract model
 * 
 * @author ghosh
 *
 */
@Component
public class ContractMapperImpl implements ContractMapper {
	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.ContractMapper#convertMerticsToGetSensonMetrics(com.assessment.co2.sensor.domain.model.Metrics)
	 */
	@Override
	public GetSensorMetrics convertSummarizedSensorDayToGetSensonMetrics(SummarizedSensorDay summaRizedSensorday) {
		GetSensorMetrics getSensorMetrics = new GetSensorMetrics();
		getSensorMetrics.setAvgLast30Days(summaRizedSensorday.getAverage());
		getSensorMetrics.setMaxLast30Days(summaRizedSensorday.getMax());
		return getSensorMetrics;
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.ContractMapper#convertAlertToGetAlert(com.assessment.co2.sensor.domain.model.SensorStatus)
	 */
	@Override
	public GetAlerts convertSensorStatusToGetAlert(SensorStatus sensorStatus) {
		GetAlerts getAlerts = new GetAlerts();
		if (sensorStatus != null && sensorStatus.getAlerts()!=null && sensorStatus.getAlerts().size()>0) {
			DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

			getAlerts.setStartDate(fmt.print(sensorStatus.getAlertStartTime()));
			getAlerts.setEndDate(fmt.print(sensorStatus.getAlertEndTime()));

			List<Integer> measurements = new LinkedList<Integer>();

			measurements = sensorStatus.getAlerts();

			getAlerts.setMesurement(measurements);
		}
		return getAlerts;
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.ContractMapper#convertStatusToGetStatus(com.assessment.co2.sensor.domain.model.Status)
	 */
	@Override
	public GetStatus convertStatusToGetStatus(Status status) {
		GetStatus getStatus = new GetStatus();
		getStatus.setStatus(status);
		return getStatus;
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.ContractMapper#convertMeasurementToSensorStatus(com.assessment.co2.sensor.server.contract.Mesurment)
	 */
	@Override
	public Sensor convertMeasurementToSensorStatus(Mesurment measurement) {
		Sensor sensor = new Sensor();
		sensor.setTime(measurement.getTime());
		sensor.setLevel(measurement.getCo2());
		return sensor;
	}
}
