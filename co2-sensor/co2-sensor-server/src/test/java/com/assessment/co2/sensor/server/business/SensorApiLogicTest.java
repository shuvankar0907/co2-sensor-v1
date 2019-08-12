/**
 * 
 */
package com.assessment.co2.sensor.server.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.business.SensorManager;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.server.contract.GetAlerts;
import com.assessment.co2.sensor.server.contract.GetSensorMetrics;
import com.assessment.co2.sensor.server.contract.GetStatus;
import com.assessment.co2.sensor.server.contract.Mesurment;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SensorApiLogicTest {

	@InjectMocks
	SensorApiLogicImpl sensorApiLogic;
	@Mock
	SensorManager sensorManager;
	@Mock
	RabbitMQManager rabbitMQManager;
	@Mock
	ContractMapper contractMapper;
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
	 * Test method for {@link com.assessment.co2.sensor.server.business.SensorApiLogicImpl#saveSensorLevel(java.lang.String, com.assessment.co2.sensor.server.contract.Mesurment)}.
	 */
	@Test
	public void testSaveSensorLevel() {
		String sensorid=UUID.randomUUID().toString();
		Mesurment measurement=new Mesurment();
		Sensor sensorMock=mock(Sensor.class);
		when(contractMapper.convertMeasurementToSensorStatus(measurement)).thenReturn(sensorMock);
		doNothing().when(rabbitMQManager).sendMsg(sensorMock);
		ResponseEntity<Void> res= sensorApiLogic.saveSensorLevel(sensorid, measurement);
		
		assertEquals(HttpStatus.OK,res.getStatusCode());
		
	}
	
	@Test(expected = Exception.class)
	public void testSaveSensorLevelThrowException() {
		String sensorid=UUID.randomUUID().toString();
		Mesurment measurement=new Mesurment();
		Sensor sensorMock=mock(Sensor.class);
		when(contractMapper.convertMeasurementToSensorStatus(measurement)).thenThrow(new Exception());
		doNothing().when(rabbitMQManager).sendMsg(sensorMock);
		ResponseEntity<Void> res= sensorApiLogic.saveSensorLevel(sensorid, measurement);
		
		assertEquals(HttpStatus.OK,res.getStatusCode());
		
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.SensorApiLogicImpl#getSensorStatus(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetSensorStatus() throws Exception {
		UUID sensorId=UUID.randomUUID();
		Status status=Status.OK;
		GetStatus getStatus=new GetStatus();
		getStatus.setStatus(Status.OK);
		when(contractMapper.convertStatusToGetStatus(status)).thenReturn(getStatus);
		when(sensorManager.getSensorStatus(sensorId)).thenReturn(status);
		ResponseEntity<GetStatus> res= sensorApiLogic.getSensorStatus(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
		assertEquals("OK",((GetStatus)res.getBody()).getStatus());
	}
	
	@Test(expected = Exception.class)
	public void testGetSensorStatusThrowError() throws Exception {
		UUID sensorId=UUID.randomUUID();
		Status status=Status.OK;
		GetStatus getStatus=new GetStatus();
		getStatus.setStatus(Status.OK);
		doThrow(new Exception()).when(contractMapper.convertStatusToGetStatus(status));
		when(sensorManager.getSensorStatus(sensorId)).thenReturn(status);
		ResponseEntity<GetStatus> res= sensorApiLogic.getSensorStatus(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
		assertEquals("OK",((GetStatus)res.getBody()).getStatus());
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.SensorApiLogicImpl#getSensorMetrics(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetSensorMetrics() throws Exception {
		UUID sensorId=UUID.randomUUID();
		SummarizedSensorDay summarizedSensorDay=new SummarizedSensorDay();
		summarizedSensorDay.setAverage(2300);
		summarizedSensorDay.setMax(2900);
		GetSensorMetrics getSensorMetrics=new GetSensorMetrics();
		getSensorMetrics.setAvgLast30Days(2300);
		getSensorMetrics.setMaxLast30Days(2900);
		when(contractMapper.convertSummarizedSensorDayToGetSensonMetrics(summarizedSensorDay)).thenReturn(getSensorMetrics);
		when(sensorManager.getSensorMetrics(sensorId)).thenReturn(summarizedSensorDay);
		ResponseEntity<GetSensorMetrics> res= sensorApiLogic.getSensorMetrics(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
		assertEquals(summarizedSensorDay.getAverage(),((GetSensorMetrics)res.getBody()).getAvgLast30Days());
		assertEquals(summarizedSensorDay.getMax(),((GetSensorMetrics)res.getBody()).getMaxLast30Days());
	}
	
	@Test(expected = Exception.class)
	public void testGetSensorMetricsThrowError() throws Exception {
		UUID sensorId=UUID.randomUUID();
		SummarizedSensorDay summarizedSensorDay=new SummarizedSensorDay();
		summarizedSensorDay.setAverage(2300);
		summarizedSensorDay.setMax(2900);
		GetSensorMetrics getSensorMetrics=new GetSensorMetrics();
		getSensorMetrics.setAvgLast30Days(2300);
		getSensorMetrics.setMaxLast30Days(2900);
		when(contractMapper.convertSummarizedSensorDayToGetSensonMetrics(summarizedSensorDay)).thenReturn(getSensorMetrics);
		doThrow(new Exception()).when(sensorManager.getSensorMetrics(sensorId));
		ResponseEntity<GetSensorMetrics> res= sensorApiLogic.getSensorMetrics(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
		assertEquals(summarizedSensorDay.getAverage(),((GetSensorMetrics)res.getBody()).getAvgLast30Days());
		assertEquals(summarizedSensorDay.getMax(),((GetSensorMetrics)res.getBody()).getMaxLast30Days());
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.SensorApiLogicImpl#getAlerts(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testGetAlerts() throws Exception {
		UUID sensorId=UUID.randomUUID();
		GetAlerts getAlerts=new GetAlerts();
		List<Integer> alerts=new ArrayList<Integer>();
		alerts.add(2000);
		alerts.add(2100);
		getAlerts.setMesurement(alerts);
		SensorStatus sensorStatus=new SensorStatus();
		List<Integer> sensorAlerts=new ArrayList<Integer>();
		alerts.add(2000);
		alerts.add(2100);
		sensorStatus.setAlerts(sensorAlerts);
		
		when(contractMapper.convertSensorStatusToGetAlert(sensorManager.getAlerts(sensorId))).thenReturn(getAlerts);
		//when(contractMapper.convertStatusToGetStatus(status)).thenReturn(getStatus);
		when(sensorManager.getAlerts(sensorId)).thenReturn(sensorStatus);
		ResponseEntity<GetAlerts> res=sensorApiLogic.getAlerts(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
	
	}
	
	@Test(expected = Exception.class)
	public void testGetAlertsThrowError() throws Exception {
		UUID sensorId=UUID.randomUUID();
		GetAlerts getAlerts=new GetAlerts();
		List<Integer> alerts=new ArrayList<Integer>();
		alerts.add(2000);
		alerts.add(2100);
		getAlerts.setMesurement(alerts);
		SensorStatus sensorStatus=new SensorStatus();
		List<Integer> sensorAlerts=new ArrayList<Integer>();
		alerts.add(2000);
		alerts.add(2100);
		sensorStatus.setAlerts(sensorAlerts);
		
		when(contractMapper.convertSensorStatusToGetAlert(sensorManager.getAlerts(sensorId))).thenReturn(getAlerts);
		//when(contractMapper.convertStatusToGetStatus(status)).thenReturn(getStatus);
		doThrow(new Exception()).when(sensorManager.getAlerts(sensorId));
		ResponseEntity<GetAlerts> res=sensorApiLogic.getAlerts(sensorId.toString());
		assertEquals(HttpStatus.OK,res.getStatusCode());
	
	}

}
