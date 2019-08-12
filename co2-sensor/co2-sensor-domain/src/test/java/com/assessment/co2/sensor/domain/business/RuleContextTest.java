/**
 * 
 */
package com.assessment.co2.sensor.domain.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.business.RuleContext;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RuleContextTest {
	@Mock
	SensorStatusRepository sensorStatusRepository;
	Sensor newSensordata;
	RuleContext ruleContext;
	

	/**
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		newSensordata=getSensorData(UUID.randomUUID(),150,Status.OK,false);
		ruleContext=new RuleContext(newSensordata, sensorStatusRepository);
	}
	
	private Sensor getSensorData(UUID sensorId,int level,Status status,boolean isAlert) {
		Sensor sensor=new Sensor();
		sensor.setId(UUID.randomUUID());
		sensor.setSensorId(sensorId);
		sensor.setLevel(level);
		sensor.setRecordingDateTime(LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault()));
		sensor.setStatus(status);
		sensor.setAlert(isAlert);
		return sensor;
	}
	
	private SensorStatus getSensorStatus(int lastValue,int lastMinusOneValue,Status status,UUID sensorId) {
		SensorStatus sensorStatus=new SensorStatus();
		sensorStatus.setLastValue(lastValue);
		sensorStatus.setLastMinusOneValue(lastMinusOneValue);
		sensorStatus.setStatus(status);
		sensorStatus.setSensorId(sensorId);
		return sensorStatus;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.domain.domain.business.RuleContext#getCurrentStatus(int)}.
	 */
	@Test
	public void testGetCurrentStatusWhenStatusDoesNotExist() {
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(null);
		Status status=ruleContext.getCurrentSensorStatus();
		assertEquals(status, Status.OK);
		
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(null);
		
	}
	
	@Test
	public void testGetCurrentStatus() {
		SensorStatus sensorStatus=getSensorStatus(120,150,Status.OK,newSensordata.getSensorId());
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(sensorStatus);
		Status status=ruleContext.getCurrentSensorStatus();
		assertEquals(status, Status.OK);
		
		//when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(null);
		
	}
	@Test
	public void testgetPreviousXreadingsWhenNOReadingIsAvailable() {
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(null);
		int[] readings=ruleContext.getPreviousXreadings();
		assertEquals(ruleContext.noOfPreviousRecordToCheck(),readings.length);
		assertEquals(-1,readings[0]);
		assertEquals(-1,readings[1]);
	}
	@Test
	public void testgetPreviousXreadingsWhenOnlyOneReadingIsAvailable() {
		SensorStatus sensorStatus=getSensorStatus(120,-1,Status.OK,newSensordata.getSensorId());
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(sensorStatus);
		int[] readings=ruleContext.getPreviousXreadings();
		assertEquals(ruleContext.noOfPreviousRecordToCheck(),readings.length);
		assertEquals(120,readings[0]);
		assertEquals(-1,readings[1]);
	}
	@Test
	public void testgetPreviousXreadingsWhenBothReadingsAreAvailable() {
		SensorStatus sensorStatus=getSensorStatus(220,230,Status.ALERT,newSensordata.getSensorId());
		when(sensorStatusRepository.getSensorStatus(newSensordata.getSensorId())).thenReturn(sensorStatus);
		int[] readings=ruleContext.getPreviousXreadings();
		assertEquals(ruleContext.noOfPreviousRecordToCheck(),readings.length);
		assertEquals(220,readings[0]);
		assertEquals(230,readings[1]);
	}
	@Test
	public void testgetNewCo2level() {
		int co2Level=189;
		Sensor sensor=getSensorData(UUID.randomUUID(),co2Level,Status.OK,false);
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		assertEquals(co2Level,ruleContext.getNewCo2level());
	}
	
	@Test
	public void testsetNewAlertStatus() {
		boolean isalert=true;
		Sensor sensor=getSensorData(UUID.randomUUID(),180,Status.OK,isalert);
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		assertEquals(isalert,ruleContext.getNewSensor().isAlert());
	}
	
	@Test
	public void testgetCurrentSensorStatus() {
		Status status =Status.OK;
		Sensor sensor=getSensorData(UUID.randomUUID(),180,status,false);
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		assertEquals(status,ruleContext.getCurrentSensorStatus());
	}
	
	@Test
	public void testGetNewSensorStatusNoNewAlerts() {
		Status status =Status.OK;
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=getSensorData(sensorId,180,status,false);
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		SensorStatus sensorStatus=getSensorStatus(220,230,Status.ALERT,sensorId);
		when(sensorStatusRepository.getSensorStatus(sensorId)).thenReturn(sensorStatus);
		
		SensorStatus newSensorStatus=ruleContext.getNewSensorStatus();
		
		assertEquals(sensorId,newSensorStatus.getSensorId());
		assertEquals(sensor.getLevel(),newSensorStatus.getLastValue());
		assertEquals(220,newSensorStatus.getLastMinusOneValue());
		//assertFalse(newSensorStatus)
	}
	@Test
	public void testGetNewSensorStatusFirstAlertRecorded() {
		Status status =Status.OK;
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=getSensorData(sensorId,240,status,true);
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		sensor.setRecordingDateTime(sdate);
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		SensorStatus sensorStatus=getSensorStatus(220,230,Status.ALERT,sensorId);
		when(sensorStatusRepository.getSensorStatus(sensorId)).thenReturn(sensorStatus);
		
		SensorStatus newSensorStatus=ruleContext.getNewSensorStatus();
		
		assertEquals(sensorId,newSensorStatus.getSensorId());
		assertEquals(sensor.getLevel(),newSensorStatus.getLastValue());
		assertEquals(220,newSensorStatus.getLastMinusOneValue());
		assertEquals(sdate,newSensorStatus.getAlertStartTime());
		assertEquals(sdate,newSensorStatus.getAlertEndTime());
		assertEquals((Integer)sensor.getLevel(),newSensorStatus.getAlerts().get(0));
		//assertFalse(newSensorStatus)
	}
	
	@Test
	public void testGetNewSensorStatusNotFirstAlertRecorded() {
		Status status =Status.OK;
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=getSensorData(sensorId,240,status,true);
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		sensor.setRecordingDateTime(sdate.plus(5));
		ruleContext=new RuleContext(sensor, sensorStatusRepository);
		
		SensorStatus sensorStatus=getSensorStatus(220,230,Status.ALERT,sensorId);
		List<Integer> alerts=new ArrayList<Integer>();
		alerts.add(250);
		alerts.add(280);
		sensorStatus.setAlerts(alerts);
		sensorStatus.setAlertStartTime(sdate);
		sensorStatus.setAlertEndTime(sdate.plusDays(3));
		when(sensorStatusRepository.getSensorStatus(sensorId)).thenReturn(sensorStatus);
		
		SensorStatus newSensorStatus=ruleContext.getNewSensorStatus();
		
		assertEquals(sensorId,newSensorStatus.getSensorId());
		assertEquals(sensor.getLevel(),newSensorStatus.getLastValue());
		assertEquals(220,newSensorStatus.getLastMinusOneValue());
		assertEquals(sensorStatus.getAlertStartTime(),newSensorStatus.getAlertStartTime());
		assertEquals(sensor.getRecordingDateTime(),newSensorStatus.getAlertEndTime());
		assertEquals(3,newSensorStatus.getAlerts().size());
		//assertFalse(newSensorStatus)
	}

}
