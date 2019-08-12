package com.assessment.co2.sensor.domain.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.model.SensorStatus;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SensorStatusRepositoryImplTest {
	private static final String SENSOR_STATUS_COLLECTION_NAME = "SensorStatus";
	@InjectMocks
	SensorStatusRepositoryImpl sensorStatusRepository;

	@Mock
	MongoOperations mongoTemplate;

	@Test
	public void testGetSensorStatus() {
		UUID sensorId = UUID.randomUUID();
		SensorStatus sensorStatus = new SensorStatus();
		sensorStatus.setSensorId(sensorId);
		when(mongoTemplate.findOne(any(), eq(SensorStatus.class), eq(SENSOR_STATUS_COLLECTION_NAME)))
				.thenReturn(sensorStatus);

		SensorStatus ressensorStatus = sensorStatusRepository.getSensorStatus(sensorId);

		assertNotNull(ressensorStatus);
		assertEquals(sensorId, ressensorStatus.getSensorId());

	}

	@Test
	public void testSave() {
		UUID sensorId = UUID.randomUUID();
		SensorStatus sensorStatus = new SensorStatus();
		sensorStatus.setSensorId(sensorId);
		doNothing().when(mongoTemplate).save(sensorStatus, SENSOR_STATUS_COLLECTION_NAME);

		sensorStatusRepository.save(sensorStatus);
		verify(mongoTemplate,times(1)).save(sensorStatus,SENSOR_STATUS_COLLECTION_NAME);
	}

}
