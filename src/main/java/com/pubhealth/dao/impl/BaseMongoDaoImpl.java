package com.pubhealth.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pubhealth.dao.BaseMongoDao;

/*
 * @author melo
*/

@Repository("baseMongoDaoImpl")
public class BaseMongoDaoImpl implements BaseMongoDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insert(Object object, String collectionName) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public Object findOne(Map<String, Object> params, String collectionName, Class cls) {
		DBObject obj = new BasicDBObject();
		params.entrySet().forEach(item -> obj.put(item.getKey(), item.getValue()));
		Query query = new BasicQuery(obj);
		Object record = mongoTemplate.findOne(query, cls, collectionName);
		return record;
	}

	@Override
	public Object findByObjectId(String id, String collectionName, Class cls) {
		DBObject obj = new BasicDBObject();
		ObjectId objId = new ObjectId(id);
		obj.put("_id", objId);
		Query query = new BasicQuery(obj);
		Object record = mongoTemplate.findOne(query, cls, collectionName);
		return record;
	}

	@Override
	public List<Object> findAll(Map<String, Object> params, String collectionName) {
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
