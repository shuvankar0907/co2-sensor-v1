package com.assessment.co2.sensor.domain.business;

import java.util.List;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;

/**
 * This is the helper class for Rule. This class is responsible for all the
 * collecting all the data required for rule execution
 * 
 * @author ghosh
 *
 */
public class RuleContext {
	private SensorStatusRepository sensorStatusRepository;
	private Sensor newSensor;
	private SensorStatus sensorStatus;
	private final int CONSECUTIVE_RECORD_TO_CHECK = 2;

	public RuleContext(Sensor newSensor, SensorStatusRepository sensorStatusRepository) {
		this.sensorStatusRepository = sensorStatusRepository;
		this.newSensor = newSensor;
	}
	/**
	 * Number of previous records required for rule execution
	 * @return
	 */
	public int noOfPreviousRecordToCheck() {
		return CONSECUTIVE_RECORD_TO_CHECK;
	}
	/**
	 * Sets the new calculated status 
	 * @param status
	 */
	public void setStatus(Status status) {
		newSensor.setStatus(status);
	}
	/**
	 * Get the new sensor object
	 * @return
	 */
	public Sensor getNewSensor() {
		return newSensor;
	}
	/** 
	 * Set the alert status after the rule execution
	 * @param alertStatus
	 */
	public void setNewAlertStatus(boolean alertStatus) {
		newSensor.setAlert(alertStatus);
	}
	/**
	 * Current sensor status
	 * @return
	 */
	public Status getCurrentSensorStatus() {
		return getSensorStatus().getStatus();
	}
	/**
	 * Co2 level of the new sensor data
	 * @return
	 */
	public int getNewCo2level() {
		return newSensor.getLevel();
	}
	/**
	 * Gets the X number of previous records
	 * @return
	 */
	public int[] getPreviousXreadings() {
		int[] sensorlevels = new int[CONSECUTIVE_RECORD_TO_CHECK];
		sensorlevels[0] = getSensorStatus().getLastValue();
		sensorlevels[1] = getSensorStatus().getLastMinusOneValue();
		return sensorlevels;
	}
	/**
	 * Gets the sensor status object after rule execution
	 * @return
	 */
	public SensorStatus getNewSensorStatus() {
		SensorStatus newSensorStatus = new SensorStatus();
		newSensorStatus.setSensorId(newSensor.getSensorId());
		newSensorStatus.setStatus(newSensor.getStatus());
		newSensorStatus.setSensorId(newSensor.getSensorId());
		newSensorStatus.setLastValue(newSensor.getLevel());
		newSensorStatus.setLastMinusOneValue(getSensorStatus().getLastValue());
		if(newSensor.isAlert()) {
			List<Integer> alerts=getSensorStatus().getAlerts();
			alerts.add(newSensor.getLevel());
			newSensorStatus.setAlerts(alerts);
			newSensorStatus.setAlertEndTime(newSensor.getRecordingDateTime());
			if(getSensorStatus().getAlertStartTime()==null) {
				newSensorStatus.setAlertStartTime(newSensor.getRecordingDateTime());
			}
			else {
				newSensorStatus.setAlertStartTime(getSensorStatus().getAlertStartTime());
			}
		}
		else {
			newSensorStatus.setAlertEndTime(getSensorStatus().getAlertEndTime());
			newSensorStatus.setAlertStartTime(getSensorStatus().getAlertStartTime());
			newSensorStatus.setAlerts(getSensorStatus().getAlerts());
		}
		return newSensorStatus;
	}

	private SensorStatus getSensorStatus() {
		if (sensorStatus == null) {
			sensorStatus = sensorStatusRepository.getSensorStatus(newSensor.getSensorId());
			if (sensorStatus == null) {
				sensorStatus = new SensorStatus();
			}
		}
		return sensorStatus;
	}

}
