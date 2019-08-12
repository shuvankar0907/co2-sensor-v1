/**
 * 
 */
package com.assessment.co2.sensor.domain.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.SummarizedSensorDay;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SummarizedSensorDayRepositoryImplTest {
	
	private static final String SUMMARIZED_COLLECTION_NAME = "SummarizedSensorDay";
	
	@InjectMocks
	SummarizedSensorDayRepositoryImpl summarizedSensorDayRepositoryImpl;
	
	@Mock
	 MongoTemplate mongoTemplate;
	
	

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
	 * Test method for {@link com.assessment.co2.sensor.domain.repository.SummarizedSensorDayRepositoryImpl#saveAllSummarizedSensorDayRepository(java.util.List)}.
	 */
	@Test
	public void testSaveAllSummarizedSensorDayRepository() {
		DBCollection collection =mock(DBCollection.class);
		BulkWriteOperation bulkWriteOperationInserts =mock(BulkWriteOperation.class);
		when(mongoTemplate.getCollection(SUMMARIZED_COLLECTION_NAME)).thenReturn(collection);
		when(collection.initializeUnorderedBulkOperation()).thenReturn(bulkWriteOperationInserts);
		com.mongodb.BulkWriteResult bulkWriteResult =mock(com.mongodb.BulkWriteResult.class);
		
		when(bulkWriteOperationInserts.execute()).thenReturn(bulkWriteResult);
		
		when(bulkWriteResult.getInsertedCount()).thenReturn(1);
		List<SummarizedSensorDay> summarizedSensorDays=new ArrayList<SummarizedSensorDay>();
		
		int val=summarizedSensorDayRepositoryImpl.saveAllSummarizedSensorDayRepository(summarizedSensorDays);
		assertEquals(val,1);
		verify(bulkWriteOperationInserts,times(1)).execute();
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.domain.repository.SummarizedSensorDayRepositoryImpl#getRangeOfdaysSummarizedMetrix(java.util.UUID, org.joda.time.DateTime, org.joda.time.DateTime)}.
	 */
	@Test
	public void testGetRangeOfdaysSummarizedMetrix() {
		UUID sensorId=UUID.randomUUID();

		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime edate=LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		//Aggregation aggregation =mock(Aggregation.class);
		List<SummarizedSensorDay> summarizedSensorDays=new ArrayList<SummarizedSensorDay>();
		SummarizedSensorDay summarizedSensorDay=new SummarizedSensorDay();
		summarizedSensorDay.setSensorId(sensorId);
		summarizedSensorDays.add(summarizedSensorDay);
		DBObject dbObject=new BasicDBObject();
		AggregationResults<SummarizedSensorDay> results =new AggregationResults<SummarizedSensorDay>(summarizedSensorDays,dbObject);
		
		when(mongoTemplate.aggregate(any(Aggregation.class),
				eq(SUMMARIZED_COLLECTION_NAME),
	            eq(SummarizedSensorDay.class))).thenReturn(results);
		
		SummarizedSensorDay summarizedSensorDayret= summarizedSensorDayRepositoryImpl.getRangeOfdaysSummarizedMetrix(sensorId, sdate, edate);
		assertNotNull(summarizedSensorDayret);
	}
	
	@Test
	public void testGetRangeOfdaysSummarizedMetrixNoResultFound() {
		UUID sensorId=UUID.randomUUID();

		DateTime sdate=LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		DateTime edate=LocalDate.now().plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.getDefault());
		//Aggregation aggregation =mock(Aggregation.class);
		List<SummarizedSensorDay> summarizedSensorDays=new ArrayList<SummarizedSensorDay>();
		DBObject dbObject=new BasicDBObject();
		AggregationResults<SummarizedSensorDay> results =new AggregationResults<SummarizedSensorDay>(summarizedSensorDays,dbObject);
		
		when(mongoTemplate.aggregate(any(Aggregation.class),
				eq(SUMMARIZED_COLLECTION_NAME),
	            eq(SummarizedSensorDay.class))).thenReturn(results);
		
		SummarizedSensorDay summarizedSensorDayret= summarizedSensorDayRepositoryImpl.getRangeOfdaysSummarizedMetrix(sensorId, sdate, edate);
		assertNotNull(summarizedSensorDayret);
	}

}
