package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.HealthEdu;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.RangeField;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class HealthEduService {
	Index index = new Index("health_education", "health_education");
	
	@Autowired
	private ESQueryWrapper dao;
	public String search(HealthEdu healthEdu){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(healthEdu.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", healthEdu.getProvinceId(), ESSearchType.FILTER));
		}
		if(healthEdu.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", healthEdu.getCityId(), ESSearchType.FILTER));
		}
		if(healthEdu.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", healthEdu.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(healthEdu.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", healthEdu.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(healthEdu.getPersonalName())){
			param.fieldList.add(new TermField("organizer.keyword", healthEdu.getPersonalName(), ESSearchType.FILTER));
		}
		
		if(StringUtils.isNotEmpty(healthEdu.getFromTime()) || StringUtils.isNotEmpty(healthEdu.getToTime())) {
			param.fieldList.add(new RangeField("time", healthEdu.getFromTime(), true, healthEdu.getToTime(), true, ESSearchType.MUST));
		}
		
		if(StringUtils.isNotEmpty(healthEdu.getUid())) {
			param.fieldList.add(new TermField("uid", healthEdu.getUid(), ESSearchType.FILTER));
		}
		
		if(healthEdu.getFirstIndex()>0){
			param.setFrom(healthEdu.getFirstIndex());
		}
		param.setSize(healthEdu.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
