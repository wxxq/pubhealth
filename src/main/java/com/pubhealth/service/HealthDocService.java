package com.pubhealth.service;

import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Index;
import com.pubhealth.util.ESConnector;

@Service
public class HealthDocService {
	
	Index index = new Index("school", "students");
	ESQueryWrapper dao = new ESQueryWrapper(ESConnector.getClient(), index);
	
	public String searchHealthDoc(){
		return "test";
	}
}
