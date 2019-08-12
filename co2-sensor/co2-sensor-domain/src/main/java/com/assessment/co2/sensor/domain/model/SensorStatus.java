/**
 * 
 */
package com.assessment.co2.sensor.domain.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

/**
 * Sensor status collection
 * @author ghosh
 *
 */
public class SensorStatus {
	@Id
	private UUID sensorId;
	private Status status=Status.OK;
	private int lastValue=-1;
	private int lastMinusOneValue=-1;
	private DateTime alertStartTime;
	private DateTime alertEndTime;
	private List<Integer> alerts=new LinkedList<Integer>();
	/**
	 * @return the alertStartTime
	 */
	public DateTime getAlertStartTime() {
		return alertStartTime;
	}
	/**
	 * @param alertStartTime the alertStartTime to set
	 */
	public void setAlertStartTime(DateTime alertStartTime) {
		this.alertStartTime = alertStartTime;
	}
	/**
	 * @return the alertEndTime
	 */
	public DateTime getAlertEndTime() {
		return alertEndTime;
	}
	/**
	 * @param alertEndTime the alertEndTime to set
	 */
	public void setAlertEndTime(DateTime alertEndTime) {
		this.alertEndTime = alertEndTime;
	}
	/**
	 * @return the alerts
	 */
	public List<Integer> getAlerts() {
		return alerts;
	}
	/**
	 * @param alerts the alerts to set
	 */
	public void setAlerts(List<Integer> alerts) {
		this.alerts = alerts;
	}
	/**
	 * @return the sensorId
	 */
	public UUID getSensorId() {
		return sensorId;
	}
	/**
	 * @param sensorId the sensorId to set
	 */
	public void setSensorId(UUID sensorId) {
		this.sensorId = sensorId;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the lastValue
	 */
	public int getLastValue() {
		return lastValue;
	}
	/**
	 * @param lastValue the lastValue to set
	 */
	public void setLastValue(int lastValue) {
		this.lastValue = lastValue;
	}
	/**
	 * @return the lastMinusOneValue
	 */
	public int getLastMinusOneValue() {
		return lastMinusOneValue;
	}
	/**
	 * @param lastMinusOneValue the lastMinusOneValue to set
	 */
	public void setLastMinusOneValue(int lastMinusOneValue) {
		this.lastMinusOneValue = lastMinusOneValue;
	}
	

}
