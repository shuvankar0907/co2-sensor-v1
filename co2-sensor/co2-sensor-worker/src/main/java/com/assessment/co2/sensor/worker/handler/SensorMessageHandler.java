package com.assessment.co2.sensor.worker.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


import com.assessment.co2.sensor.domain.business.SensorManager;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * Handles messages from rabbit. Stores the messages in memory till more then
 * one data arrives from the queue. Otherwise it doesn't send ack to rabbit mq
 * It sends the data to manager and sends the ack to rabbit mq
 * 
 * @author ghosh
 *
 */
@Component
public class SensorMessageHandler implements DeliverCallback {
	SensorManager sensorManager;
	Converter converter;

	@Autowired
	public SensorMessageHandler(SensorManager sensorManager,Converter converter) {
		this.sensorManager = sensorManager;
		this.converter=converter;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.assessment.co2.sensor.worker.handler.SensorMessageHandler#handleMessage(
	 * com.assessment.co2.sensor.domain.model.Sensor)
	 */

	@Override
	public void handle(String consumerTag, Delivery message) throws IOException {
		Sensor sensor =converter.getSensorObjFromBytes(message.getBody());
		try {
			sensorManager.saveSensor(sensor);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
