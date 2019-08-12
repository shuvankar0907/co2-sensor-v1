/**
 * 
 */
package com.assessment.co2.sensor.domain.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.data.mongodb.core.BulkOperations;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SensorRepositoryImplTest {

	@InjectMocks
	SensorRepositoryImpl sensorRepository;
	@Mock
	private MongoOperations mongoTemplate;

	private static final String SENSOR_COLLECTION_NAME = "Sensor";

	/**
	 * Test method for
	 * {@link com.assessment.co2.sensor.domain.repository.SensorRepositoryImpl#saveLevel(com.assessment.co2.sensor.domain.model.Sensor)}.
	 */
	@Test
	public void testSaveLevel() {
		Sensor sensor = new Sensor();
		UUID sensorId = UUID.randomUUID();
		sensor.setSensorId(sensorId);

		doNothing().when(mongoTemplate).insert(sensor, SENSOR_COLLECTION_NAME);

		sensorRepository.saveLevel(sensor);
		verify(mongoTemplate,times(1)).insert(sensor,SENSOR_COLLECTION_NAME);
	}

	/**
	 * Test method for
	 * {@link com.assessment.co2.sensor.domain.repository.SensorRepositoryImpl#getPreviousXLevels(java.util.UUID, int)}.
	 */
	@Test
	public void testGetPreviousXLevels() {
		UUID sensorId=UUID.randomUUID();
		int noOfPrevRecord=2;
		List<Sensor> sensors=new ArrayList<Sensor>();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensors.add(sensor);
		
		when(mongoTemplate.find(any(), eq(Sensor.class), eq(SENSOR_COLLECTION_NAME))).thenReturn(sensors);
		List<Sensor>sensorsRet= sensorRepository.getPreviousXLevels(sensorId, noOfPrevRecord);
		assertNotNull(sensorsRet);
		assertEquals(sensors, sensorsRet);
	}

	/**
	 * Test method for
	 * {@link com.assessment.co2.sensor.domain.repository.SensorRepositoryImpl#getAggregatedData(org.joda.time.DateTime, org.joda.time.DateTime)}.
	 */
	@Test
	public void testGetAggregatedData() {
		UUID sensorId=UUID.randomUUID();

		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime edate=LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		List<SummarizedSensorDay> summarizedSensorDays=new ArrayList<SummarizedSensorDay>();
		SummarizedSensorDay summarizedSensorDay=new SummarizedSensorDay();
		summarizedSensorDay.setSensorId(sensorId);
		summarizedSensorDays.add(summarizedSensorDay);
		DBObject dbObject=new BasicDBObject();
		AggregationResults<SummarizedSensorDay> results =new AggregationResults<SummarizedSensorDay>(summarizedSensorDays,dbObject);
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//		DateTime startdate=sdate;
		
		when(mongoTemplate.aggregate(any(Aggregation.class),  eq(SENSOR_COLLECTION_NAME),eq(SummarizedSensorDay.class))).thenReturn(results);
		List<SummarizedSensorDay> resSummarizedDays= sensorRepository.getAggregatedData(sdate, edate);
		assertEquals(summarizedSensorDays,resSummarizedDays);
	}

	/**
	 * Test method for
	 * {@link com.assessment.co2.sensor.domain.repository.SensorRepositoryImpl#delete(org.joda.time.DateTime, org.joda.time.DateTime)}.
	 */
	@Test
	public void testDelete() {
		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime edate=LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		BulkOperations bulOperation=mock(BulkOperations.class);
		com.mongodb.BulkWriteResult bulkWriteResults=mock(com.mongodb.BulkWriteResult.class);
		
		when(mongoTemplate.bulkOps(BulkMode.UNORDERED, SENSOR_COLLECTION_NAME)).thenReturn(bulOperation);
		when(bulOperation.remove(any(Query.class))).thenReturn(bulOperation);
		
		when(bulOperation.execute()).thenReturn(bulkWriteResults);
		
		sensorRepository.delete(sdate, edate);
		verify(bulOperation,times(1)).execute();
	}

}
