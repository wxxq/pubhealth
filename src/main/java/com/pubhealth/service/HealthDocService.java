package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.HealthDoc;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class HealthDocService {
	
	Index index = new Index("pubhealth", "health_doc");

	@Autowired
	private ESQueryWrapper dao;
	
	public String searchHealthDoc(HealthDoc healthDoc){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(healthDoc.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", healthDoc.getProvinceId(), ESSearchType.FILTER));
		}
		if(healthDoc.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", healthDoc.getCityId(), ESSearchType.FILTER));
		}
		if(healthDoc.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", healthDoc.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(healthDoc.getIdCard())){
			param.fieldList.add(new TermField("id_card.keyword", healthDoc.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(healthDoc.getPersonalName())){
			param.fieldList.add(new TermField("personal_name.keyword", healthDoc.getPersonalName(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(healthDoc.getPhone())){
			param.fieldList.add(new TermField("phone.keyword", healthDoc.getPhone(),ESSearchType.FILTER));
		}
		
		
		if(healthDoc.getFirstIndex()>0){
			param.setFrom(healthDoc.getFirstIndex());
		}
		param.setSize(healthDoc.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
	
	public String test2(){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		TermField g = new TermField("personal_name", "侯夏瑶", ESSearchType.FILTER);
//		TermField g2 = new TermField("city_id", 25010000, ESSearchType.FILTER);
//		TermField g3 = new TermField("district_id", 35010400, ESSearchType.MUST);
//		TermField g4 = new TermField("phone", "15951570309", ESSearchType.MUST);
		param.fieldList.add(g);
//		param.fieldList.add(g2);
//		param.fieldList.add(g3);
//		param.fieldList.add(g4);
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		System.out.println(json.toString());
		return json;
	}
	
	public String test(){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
//		TermField g = new TermField("category", "class3", ESSearchType.FILTER);
		TermField g2 = new TermField("height", "143", ESSearchType.MUST);
		TermField g3 = new TermField("age", "36", ESSearchType.MUST);
//		param.fieldList.add(g);
		param.fieldList.add(g2);
		param.fieldList.add(g3);
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		System.out.println(json.toString());
		return json;
	}
}
