/**
 * 
 */
package com.assessment.co2.sensor.worker.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

import com.assessment.co2.sensor.domain.model.Sensor;

/**
 * @author ghosh
 *
 */
@Component
public class ConverterImpl implements Converter {
	
	/* (non-Javadoc)
	 * @see com.assessment.co2.sensor.worker.handler.Converter#getSensorObjFromBytes(byte[])
	 */
	@Override
	public Sensor getSensorObjFromBytes(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Sensor sensor = null;
		;
		try {
			in = new ObjectInputStream(bis);
			try {
				sensor = (Sensor) in.readObject();
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
				// Parsing the date
				sensor.setRecordingDateTime(dtf.parseDateTime(sensor.getTime()));
				sensor.setTime("");

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return sensor;
	}

}
