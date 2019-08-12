/**
 * 
 */
package com.assessment.co2.sensor.domain.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.assessment.co2.sensor.domain.model.SensorStatus;


/**
 * @author ghosh
 *
 */
@Repository
public class SensorStatusRepositoryImpl implements SensorStatusRepository {

	private final MongoOperations mongoTemplate;
	private static final String SENSOR_STATUS_COLLECTION_NAME = "SensorStatus";

	@Autowired
	public SensorStatusRepositoryImpl(MongoOperations mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/*
	 * Get current sensorStatus
	 * 
	 * @see com.assessment.co2.sensor.domain.repository.SensorStatusRepository#
	 * getSensorStatus(java.util.UUID)
	 */
	@Override
	public SensorStatus getSensorStatus(UUID sensorId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("sensorId").is(sensorId));
		
		return mongoTemplate.findOne(query, SensorStatus.class, SENSOR_STATUS_COLLECTION_NAME);
	}
	
	public void save(SensorStatus sensorStatus) {
		mongoTemplate.save(sensorStatus, SENSOR_STATUS_COLLECTION_NAME);
	}
}
