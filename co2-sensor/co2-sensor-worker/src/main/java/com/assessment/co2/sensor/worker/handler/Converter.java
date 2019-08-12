package com.assessment.co2.sensor.worker.handler;

import com.assessment.co2.sensor.domain.model.Sensor;

public interface Converter {

	Sensor getSensorObjFromBytes(byte[] bytes);

}