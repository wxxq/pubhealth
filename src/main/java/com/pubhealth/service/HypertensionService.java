package com.pubhealth.service;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pubhealth.dao.ESQueryWrapper;
import com.pubhealth.entity.Hypertension;
import com.pubhealth.entity.Index;
import com.pubhealth.entity.InfectionDoc;
import com.pubhealth.entity.ES.ESParam;
import com.pubhealth.entity.ES.ESSearchType;
import com.pubhealth.entity.ES.TermField;
import com.pubhealth.util.ESResponseParse;

@Service
public class HypertensionService {
	Index index = new Index("hypertension_fu", "hypertension_fu");

	@Autowired
	private ESQueryWrapper dao;
	
	public String search(Hypertension hypertension){
		ESParam param = new ESParam();
		param.fieldList = new ArrayList<>();
		if(hypertension.getProvinceId()!=0){
			param.fieldList.add(new TermField("province_id", hypertension.getProvinceId(), ESSearchType.FILTER));
		}
		if(hypertension.getCityId()!=0){
			param.fieldList.add(new TermField("city_id", hypertension.getCityId(), ESSearchType.FILTER));
		}
		if(hypertension.getDistrictId()!=0){
			param.fieldList.add(new TermField("district_id", hypertension.getDistrictId(), ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(hypertension.getIdCard())){
			param.fieldList.add(new TermField("create_id_card.keyword", hypertension.getIdCard(),ESSearchType.FILTER));
		}
		if(StringUtils.isNotEmpty(hypertension.getPersonalName())){
			param.fieldList.add(new TermField("doctor_signature.keyword", hypertension.getPersonalName(), ESSearchType.FILTER));
		}
	
		if(hypertension.getFirstIndex()>0){
			param.setFrom(hypertension.getFirstIndex());
		}
		param.setSize(hypertension.getPageSize());
		SearchResponse response = dao.commonQuery(param,index);
		String json = ESResponseParse.parseJsonFromResponse(response);
		return json;
	}
}
