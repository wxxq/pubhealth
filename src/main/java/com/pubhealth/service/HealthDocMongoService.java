package com.pubhealth.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.HealthDocMongoDao;
import com.pubhealth.entity.HealthDoc;

/*
 * @author melo
*/
@Service
public class HealthDocMongoService {
	@Autowired
	private HealthDocMongoDao healthDocMongoDao;

	public void insert() {
		HealthDoc healthDoc = new HealthDoc();
		String name = "melo"+ new Random().nextInt(200);
		healthDoc.setPersonalName(name);
		healthDocMongoDao.insert(healthDoc, "test");
	}
}
