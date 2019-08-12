/**
 * 
 */
package com.assessment.co2.sensor.worker.handler;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.business.SensorManager;
import com.assessment.co2.sensor.domain.business.SensorManagerImpl;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.Status;
import com.rabbitmq.client.Delivery;


/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SensorMessageHandlerTest {

	@InjectMocks
	SensorMessageHandler sensorMessageHandle;
	@Mock
	SensorManagerImpl sensorManager;
	@Mock
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
	 * Test method for {@link com.assessment.co2.sensor.worker.handler.SensorMessageHandler#handleMessage(com.assessment.co2.sensor.domain.model.Sensor)}.
	 * @throws Exception 
	 */
	@Test
	public void testHandleMessageUniquesensorDataArrived() throws Exception {
		Sensor sensor = getSensorObkj(UUID.randomUUID());
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		String time=fmt.print(sdate);
		sensor.setTime(time);
		sensor.setLevel(2100);
		byte[] sensorbyte=sensor.getBytes();
		Delivery delivery=mock(Delivery.class);
		String consumerTag=""; 
		
		when(delivery.getBody()).thenReturn(sensorbyte);
		when(converter.getSensorObjFromBytes(sensorbyte)).thenReturn(sensor);
		
		doNothing().when(sensorManager).saveSensor(sensor);
		
		sensorMessageHandle.handle(consumerTag, delivery);
		verify(sensorManager,times(1)).saveSensor(sensor);
		
	}
	
	/**
	 * @return
	 */
	private Sensor getSensorObkj(UUID uuid) {
		DateTime date=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		Sensor sensor=new Sensor();
		sensor.setSensorId(uuid);
		sensor.setId(UUID.randomUUID());
		sensor.setLevel(230);
		sensor.setStatus(Status.WARN);
		sensor.setRecordingDateTime(date);
		return sensor;
	}
	
	

}
