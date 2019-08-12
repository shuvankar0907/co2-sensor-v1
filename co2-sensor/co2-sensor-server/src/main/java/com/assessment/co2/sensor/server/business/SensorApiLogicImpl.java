package com.assessment.co2.sensor.server.business;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.assessment.co2.sensor.domain.business.SensorManager;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.server.contract.GetAlerts;
import com.assessment.co2.sensor.server.contract.GetSensorMetrics;
import com.assessment.co2.sensor.server.contract.GetStatus;
import com.assessment.co2.sensor.server.contract.Mesurment;

/**
 * Logic for Sensor Api
 * 
 * @author ghosh
 *
 */
@Component(value = "SensorApiLogic")
public class SensorApiLogicImpl implements SensorApiLogic {
	SensorManager sensorManager;
	AsynchoronousMessage rabbitMQManager;
	ContractMapper contractMapper;

	@Autowired
	public SensorApiLogicImpl(SensorManager sensorManager, RabbitMQManager rabbitMQManager,ContractMapper contractMapper) {
		this.sensorManager = sensorManager;
		this.rabbitMQManager = rabbitMQManager;
		this.contractMapper=contractMapper;
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.SensorApiLogic#saveSensorLevel(java.lang.String, com.assessment.co2.sensor.server.contract.Mesurment)
	 */
	@Override
	public ResponseEntity<Void> saveSensorLevel(String sensorId, Mesurment measurement) {
		try {
			Sensor sensor = contractMapper.convertMeasurementToSensorStatus(measurement);
			UUID uuid = UUID.fromString(sensorId);
			sensor.setSensorId(uuid);
			rabbitMQManager.sendMsg(sensor);

			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			// Log Exception
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.SensorApiLogic#getSensorStatus(java.lang.String)
	 */
	@Override
	public ResponseEntity<GetStatus> getSensorStatus(String id) {
		try {
			UUID sensorId = UUID.fromString(id);
			Status status = sensorManager.getSensorStatus(sensorId);
			return new ResponseEntity<GetStatus>(contractMapper.convertStatusToGetStatus(status), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new GetStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.SensorApiLogic#getSensorMetrics(java.lang.String)
	 */
	@Override
	public ResponseEntity<GetSensorMetrics> getSensorMetrics(String sensorId) {
		try {
			UUID uuid = UUID.fromString(sensorId);
			SummarizedSensorDay summarizedSensorDay = sensorManager.getSensorMetrics(uuid);
			return new ResponseEntity<GetSensorMetrics>(contractMapper.convertSummarizedSensorDayToGetSensonMetrics(summarizedSensorDay),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new GetSensorMetrics(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.server.business.SensorApiLogic#getAlerts(java.lang.String)
	 */
	@Override
	public ResponseEntity<GetAlerts> getAlerts(String sensorId) {
		try {
			UUID uuid = UUID.fromString(sensorId);
			return new ResponseEntity<GetAlerts>(
					contractMapper.convertSensorStatusToGetAlert(sensorManager.getAlerts(uuid)), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
