package com.pubhealth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.HealthDoc;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.BaseField;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESConnector;
import com.pubhealth.util.ESResponseParse;

@Service
public class HealthDocService {
	
	Index index = new Index("school", "students");
	ESQueryWrapper dao = new ESQueryWrapper(ESConnector.getClient(), index);
	
	public String searchHealthDoc(HealthDoc healthDoc){
		ESParam param = new ESParam();
		List<BaseField> list = new ArrayList<>();
		if(healthDoc.getProvinceId()!=0){
			list.add(new TermField("provinve_id", healthDoc.getProvinceId()));
		}
		if(healthDoc.getCityId()!=0){
			list.add(new TermField("city_id", healthDoc.getCityId()));
		}
		if(healthDoc.getDistrictId()!=0){
			list.add(new TermField("district_id", healthDoc.getDistrictId()));
		}
		if(StringUtils.isNotEmpty(healthDoc.getIdCard())){
			list.add(new TermField("id_card", healthDoc.getIdCard()));
		}
		if(StringUtils.isNotEmpty(healthDoc.getPersonalName())){
			list.add(new TermField("personal_name", healthDoc.getPersonalName()));
		}
		param.fieldList = list ;
		if(healthDoc.getFirstIndex()>0){
			param.setFrom(healthDoc.getFirstIndex());
		}
		param.setSize(healthDoc.getPageSize());
		SearchResponse response = dao.commonQuery(param);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
