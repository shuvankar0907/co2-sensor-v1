package com.assessment.co2.sensor.domain.business;


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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.domain.repository.SensorRepository;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;
import com.assessment.co2.sensor.domain.repository.SummarizedSensorDayReposirory;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SensorManagerImplTest {

	
	@Mock
	SensorRepository sensorRepository;
	@Mock
	SummarizedSensorDayReposirory summarizedDayRepo;
	
	@Mock
	SensorStatusRepository sensorStatusRepository;
	
	@Mock
	Rule rule;
	
	@InjectMocks
	SensorManagerImpl sensorManagerImpl;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveSensor() {
		Sensor sensor=new Sensor();
		sensor.setSensorId(UUID.randomUUID());
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		sensor.setRecordingDateTime(sdate);
		RuleContext ruleContext=mock(RuleContext.class);
		SensorStatus sensorStatus=mock(SensorStatus.class);
		
		
		when(rule.executeRule(sensor)).thenReturn(ruleContext);
		when(ruleContext.getNewSensor()).thenReturn(sensor);
		when(ruleContext.getNewSensorStatus()).thenReturn(sensorStatus);
		doNothing().when(sensorRepository).saveLevel(any());
		doNothing().when(sensorStatusRepository).save(any());
				
		sensorManagerImpl.saveSensor(sensor);
		
		verify(sensorRepository,times(1)).saveLevel(sensor);
		verify(sensorStatusRepository,times(1)).save(sensorStatus);
	}

	@Test
	public void testGetSensorStatus() throws Exception {
		UUID sensorUUID=UUID.randomUUID();
		SensorStatus status=new SensorStatus();
		when(sensorStatusRepository.getSensorStatus(sensorUUID)).thenReturn(status);
		sensorManagerImpl.getSensorStatus(sensorUUID);
		verify(sensorStatusRepository,times(1)).getSensorStatus(sensorUUID);
	}
	
	@Test
	public void testgetSensorMetrics() throws Exception {
		UUID sensorId=UUID.randomUUID();
		SummarizedSensorDay summarizedSensorDay=mock(SummarizedSensorDay.class);
		when(summarizedDayRepo.getRangeOfdaysSummarizedMetrix(eq(sensorId), any(), any())).thenReturn(summarizedSensorDay);
		
		SummarizedSensorDay summarizeddayRet=sensorManagerImpl.getSensorMetrics(sensorId);
		assertEquals(summarizedSensorDay,summarizeddayRet);
		
	}
	
	@Test
	public void testGetAlerts() throws Exception {
		UUID sensorId=UUID.randomUUID();
		SensorStatus sensorStatus=mock(SensorStatus.class);
		when(sensorStatusRepository.getSensorStatus(sensorId)).thenReturn(sensorStatus);
		SensorStatus sensorStatusRet=sensorManagerImpl.getAlerts(sensorId);
		assertEquals(sensorStatus,sensorStatusRet);
	}

	@Test
	public void testGetDailySummarizedDaySensorDataNodataReturned() throws Exception {
		
		List<SummarizedSensorDay> summarizedDaysDatas =new ArrayList<SummarizedSensorDay>();
		
		when(sensorRepository.getAggregatedData(any(),any())).thenReturn(summarizedDaysDatas);
		sensorManagerImpl.getDailySummarizedDaySensorData();
		verify(summarizedDayRepo,times(0)).saveAllSummarizedSensorDayRepository(summarizedDaysDatas);
		
	}
	
	@Test
	public void testGetDailySummarizedDaySensorDataFoundData() throws Exception {
		List<SummarizedSensorDay> summarizedDaysDatas =new ArrayList<SummarizedSensorDay>();
		summarizedDaysDatas.add(new SummarizedSensorDay());
		
		when(sensorRepository.getAggregatedData(any(),any())).thenReturn(summarizedDaysDatas);
		sensorManagerImpl.getDailySummarizedDaySensorData();
		
		verify(summarizedDayRepo,times(1)).saveAllSummarizedSensorDayRepository(summarizedDaysDatas);
		//verify(sensorRepository,times(0)).delete(startDate, endDate);
	}

//	@Test
//	public void testGetAllAlerts() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDailySummarizedDaySensorData() {
//		fail("Not yet implemented");
//	}

}
