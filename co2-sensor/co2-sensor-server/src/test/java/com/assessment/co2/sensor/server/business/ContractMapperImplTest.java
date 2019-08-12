/**
 * 
 */
package com.assessment.co2.sensor.server.business;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.server.business.TestConfig;
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
public class ContractMapperImplTest {

	@Autowired
	ContractMapperImpl contractMapper;
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
	 * Test method for {@link com.assessment.co2.sensor.server.business.ContractMapperImpl#convertSummarizedSensorDayToGetSensonMetrics(com.assessment.co2.sensor.domain.model.SummarizedSensorDay)}.
	 */
	@Test
	public void testConvertSummarizedSensorDayToGetSensonMetrics() {
		SummarizedSensorDay summarizedSensorDay=new SummarizedSensorDay();
		summarizedSensorDay.setAverage(2300);
		summarizedSensorDay.setMax(2900);
		
		GetSensorMetrics getSensorMetrics=contractMapper.convertSummarizedSensorDayToGetSensonMetrics(summarizedSensorDay);
		assertEquals(summarizedSensorDay.getAverage(),getSensorMetrics.getAvgLast30Days());
		assertEquals(summarizedSensorDay.getMax(), getSensorMetrics.getMaxLast30Days());
		
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.ContractMapperImpl#convertSensorStatusToGetAlert(com.assessment.co2.sensor.domain.model.SensorStatus)}.
	 */
	@Test
	public void testConvertSensorStatusToGetAlert() {
		SensorStatus sensorStatus=new SensorStatus();
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime edate=LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		sensorStatus.setAlertStartTime(sdate);
		sensorStatus.setAlertEndTime(edate);
		List<Integer> alerts=new ArrayList<Integer>();
		alerts.add(2000);
		alerts.add(3000);
		sensorStatus.setAlerts(alerts);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

		String sDate1=fmt.print(sensorStatus.getAlertStartTime());
		String sDate2=fmt.print(sensorStatus.getAlertEndTime());
		
		GetAlerts getAlerts=contractMapper.convertSensorStatusToGetAlert(sensorStatus);
		
		assertEquals(sDate1, getAlerts.getStartDate());
		assertEquals(sDate2, getAlerts.getEndDate());
		assertEquals(sensorStatus.getAlerts(), getAlerts.getMesurement());
		
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.ContractMapperImpl#convertStatusToGetStatus(com.assessment.co2.sensor.domain.model.Status)}.
	 */
	@Test
	public void testConvertStatusToGetStatus() {
		Status status=Status.OK;
		GetStatus getStatus=contractMapper.convertStatusToGetStatus(status);
		assertEquals("OK",getStatus.getStatus());
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.server.business.ContractMapperImpl#convertMeasurementToSensorStatus(com.assessment.co2.sensor.server.contract.Mesurment)}.
	 */
	@Test
	public void testConvertMeasurementToSensor() {
		Mesurment measurement=new Mesurment();
		measurement.setCo2(2100);
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

		String sDate1=fmt.print(sdate);
		measurement.setTime(sDate1);
		Sensor sensor=contractMapper.convertMeasurementToSensorStatus(measurement);
		assertEquals(measurement.getCo2(), sensor.getLevel());
		assertEquals(measurement.getTime(), sensor.getTime());
		
	}

}
