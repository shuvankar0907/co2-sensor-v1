/**
 * 
 */
package com.assessment.co2.sensor.worker.handler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.Sensor;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ConverterImplTest {
	@Autowired
	ConverterImpl converter;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.worker.handler.ConverterImpl#getSensorObjFromBytes(byte[])}.
	 * @throws IOException 
	 */
	@Test
	public void testGetSensorObjFromBytes() throws IOException {
		DateTime date=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		Sensor sensor=new Sensor();
		sensor.setSensorId(UUID.randomUUID());
		sensor.setLevel(230);
		sensor.setRecordingDateTime(date);
		byte[] bytes=sensor.getBytes();
		Sensor retSensor=converter.getSensorObjFromBytes(bytes);
		assertEquals(sensor.getSensorId(), retSensor.getSensorId());
		assertEquals(sensor.getLevel(), retSensor.getLevel());
		assertEquals(sensor.getRecordingDateTime(), retSensor.getRecordingDateTime());
	}

}
