package com.pubhealth.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.pubhealth.dao.HealthDocMongoDao;
import com.pubhealth.entity.HealthDoc;

/*
 * @author melo
*/
@Repository("healthDocMongoDaoImpl")  
public class HealthDocMongoDaoImpl implements HealthDocMongoDao {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(HealthDoc object, String collectionName) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public HealthDoc findOne(Map<String, Object> params, String collectionName) {
		return null;
	}

	@Override
	public List<HealthDoc> findAll(Map<String, Object> params, String collectionName) {
		return null;
	}

	@Override
	public void update(Map<String, Object> params, String collectionName) {

	}

	@Override
	public void createCollection(String collectionName) {

	}

	@Override
	public void remove(Map<String, Object> params, String collectionName) {

	}

}


